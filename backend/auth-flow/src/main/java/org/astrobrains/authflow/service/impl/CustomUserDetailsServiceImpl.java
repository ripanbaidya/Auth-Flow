package org.astrobrains.authflow.service.impl;

import lombok.RequiredArgsConstructor;
import org.astrobrains.authflow.enums.UserRole;
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
import java.util.List;

/**
 * Custom implementation of {@link UserDetailsService} for authenticating both
 * Sellers and Customers based on email.
 *
 * <p>
 * Any username starting with {@code "seller_"} is treated as a Seller login.
 * Otherwise, the system checks the User table.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements UserDetailsService {
    private static final String SELLER_PREFIX = "seller_";

    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null || username.isBlank()) {
            throw new UsernameNotFoundException("Username cannot be null or empty");
        }

        // Handle seller login
        if (username.startsWith(SELLER_PREFIX)) {
            String actualUsername = username.substring(SELLER_PREFIX.length());
            Seller seller = sellerRepository.findByEmail(actualUsername)
                    .orElseThrow(() -> new UsernameNotFoundException(
                            String.format("Seller not found with email: %s", actualUsername)
                    ));

            return buildUserDetails(seller.getEmail(), seller.getPassword(), seller.getRole());
        }

        // Handle customer login
        else {
            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException(
                            String.format("User not found with email: %s", username)
                    ));
            return buildUserDetails(user.getEmail(), user.getPassword(), user.getRole());
        }
    }

    private UserDetails buildUserDetails(String email, String password, UserRole role) {
        UserRole effectiveRole = (role != null) ? role : UserRole.ROLE_CUSTOMER;
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(effectiveRole.name())
        );

        return org.springframework.security.core.userdetails.User
                .withUsername(email)
                .password(password)
                .authorities(authorities)
                .build();
    }
}
