package org.astrobrains.authflow.service.impl;

import lombok.RequiredArgsConstructor;
import org.astrobrains.authflow.enums.Role;
import org.astrobrains.authflow.model.Seller;
import org.astrobrains.authflow.model.User;
import org.astrobrains.authflow.repository.SellerRepository;
import org.astrobrains.authflow.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null || username.isBlank()) {
            throw new UsernameNotFoundException("Username cannot be null or empty");
        }

        String email = username.trim().toLowerCase();

        // Try seller first
        Seller seller = sellerRepository.findByEmail(email).orElse(null);
        if (seller != null) {
            return buildUserDetails(
                    seller.getEmail(),
                    seller.getPassword(),
                    seller.getRole() != null ? seller.getRole() : Role.ROLE_SELLER
            );
        }

        // Then try customer
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return buildUserDetails(
                user.getEmail(),
                user.getPassword(),
                user.getRole() != null ? user.getRole() : Role.ROLE_CUSTOMER
        );
    }

    private UserDetails buildUserDetails(String email, String password, Role role) {
        GrantedAuthority authority = new SimpleGrantedAuthority(role.name());
        return org.springframework.security.core.userdetails.User
                .withUsername(email)
                .password(password)
                .authorities(Collections.singletonList(authority))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
