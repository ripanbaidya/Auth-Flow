package org.astrobrains.authflow.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.astrobrains.authflow.dto.ApiResponse;
import org.astrobrains.authflow.dto.request.auth.CustomerSignupRequest;
import org.astrobrains.authflow.dto.request.auth.SellerSignupRequest;
import org.astrobrains.authflow.dto.request.otp.SendOTPRequest;
import org.astrobrains.authflow.dto.request.otp.VerifyOTPRequest;
import org.astrobrains.authflow.dto.response.auth.CustomerSignupResponse;
import org.astrobrains.authflow.dto.response.auth.SellerSignupResponse;
import org.astrobrains.authflow.dto.response.otp.SendOTPResponse;
import org.astrobrains.authflow.dto.response.otp.VerifyOTPFailResponse;
import org.astrobrains.authflow.dto.response.otp.VerifyOTPSuccessResponse;
import org.astrobrains.authflow.enums.AccountStatus;
import org.astrobrains.authflow.enums.UserRole;
import org.astrobrains.authflow.model.Seller;
import org.astrobrains.authflow.model.User;
import org.astrobrains.authflow.model.VerificationCode;
import org.astrobrains.authflow.repository.SellerRepository;
import org.astrobrains.authflow.repository.UserRepository;
import org.astrobrains.authflow.repository.VerificationCodeRepository;
import org.astrobrains.authflow.security.JwtProvider;
import org.astrobrains.authflow.service.AuthService;
import org.astrobrains.authflow.service.EmailService;
import org.astrobrains.authflow.service.TwilioSmsService;
import org.astrobrains.authflow.util.OtpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * This class will have the Implementation of authentication and authorization related operations
 * including send-otp, verify-otp, signup for both user and seller
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private static final int DEFAULT_EXPIRY = 300;
    private static final int DEFAULT_RESEND_INTERVAL = 60;

    private final VerificationCodeRepository verificationCodeRepository;
    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;
    private final PasswordEncoder passwordEncoder;
    private final TwilioSmsService twilioSmsService;
    private final CustomUserDetailsServiceImpl customUserService;
    private final JwtProvider jwtProvider;
    private final EmailService emailService;

    @Value("${otp.expiry.seconds:" + DEFAULT_EXPIRY + "}")
    private long otpExpirySeconds;

    @Value("${otp.resend.interval.seconds:" + DEFAULT_RESEND_INTERVAL + "}")
    private long otpResendIntervalSeconds;

    /**
     * This method will send an OTP to the user's email/phone.
     * @param request contains target user details (email/phone).
     * @return an API response will send to the user. with the otp.
     */
    @Override
    public ApiResponse<SendOTPResponse> sendOTP(SendOTPRequest request) {
        if (request == null ||
                (request.getEmail() == null || request.getEmail().isBlank()) &&
                        (request.getPhoneNumber() == null || request.getPhoneNumber().isBlank())) {
            log.debug("Either email or phone number is required");
            return new ApiResponse<>(false, "Either email or phone number is required", null);
        }
        log.info("email / number {}", request.getEmail() != null ? request.getEmail() : request.getPhoneNumber());
        Instant now = Instant.now();
        String otp = OtpUtil.generateOtp();
        Instant expiresAt = now.plusSeconds(otpExpirySeconds); // 5 minutes

        try {
            VerificationCode existing = null;
            String identifier; // could be email or phone

            if (request.getEmail() != null && !request.getEmail().isBlank()) {
                identifier = request.getEmail().trim().toLowerCase();
                existing = verificationCodeRepository.findByEmail(identifier);
            } else {
                identifier = request.getPhoneNumber().trim();
                existing = verificationCodeRepository.findByPhoneNumber(identifier);
            }
            log.debug("identifies {}", identifier);

            // Throttle / prevent spam
            if (existing != null && existing.getExpiresAt() != null && existing.getExpiresAt().isAfter(now)) {
                Instant createdAt = existing.getCreatedAt() == null ? now : existing.getCreatedAt();
                long secondSinceCreate = Duration.between(createdAt, now).getSeconds();

                if (secondSinceCreate < otpResendIntervalSeconds) {
                    long wait = otpResendIntervalSeconds - secondSinceCreate;
                    return new ApiResponse<>(false,
                            String.format("Please wait %d seconds before requesting a new OTP", wait),
                            null);
                }

                verificationCodeRepository.delete(existing);
            }

            // Create new VerificationCode
            VerificationCode v = VerificationCode.builder()
                    .email(request.getEmail() != null ? request.getEmail().trim().toLowerCase() : null)
                    .phoneNumber(request.getPhoneNumber() != null ? request.getPhoneNumber().trim() : null)
                    .otp(otp)
                    .createdAt(now)
                    .expiresAt(expiresAt)
                    .build();
            log.debug("verification information {}", v);

            userRepository.findByEmail(v.getEmail()).ifPresent(v::setUser);
            sellerRepository.findByEmail(v.getEmail()).ifPresent(v::setSeller);
            verificationCodeRepository.save(v);

            // Decide sending channel
            if (v.getEmail() != null) {
                emailService.sendVerificationOtpEmail(
                        v.getEmail(),
                        otp,
                        "Welcome to AuthFlow!",
                        "Your Verification Code is: " + otp
                );
            } else if (v.getPhoneNumber() != null) {
                twilioSmsService.sendOtpSms(v.getPhoneNumber(), otp);
            }

            log.info("OTP sent successfully to {}", v.getEmail() != null ? v.getEmail() : v.getPhoneNumber());
            return new ApiResponse<>(
                    true,
                    "OTP sent successfully",
                    SendOTPResponse.builder()
                            .message("OTP sent successfully")
                            .expiresIn((int) otpExpirySeconds)
                            .build()
            );

        } catch (Exception ex) {
            log.info("never enter into try block");
            return new ApiResponse<>(false, "Failed to send OTP, Please try again later.", null);
        }
    }

    @Override
    public ApiResponse<?> verifyOTP(VerifyOTPRequest request) {
        if (request == null || request.getOtp() == null ||
                ((request.getEmail() == null || request.getEmail().isBlank()) &&
                        (request.getPhoneNumber() == null || request.getPhoneNumber().isBlank()))) {
            log.debug("Email/Phone & OTP are required");
            return new ApiResponse<>(false, "Email/Phone & OTP are required", null);
        }

        String providedOtp = request.getOtp().trim();
        Instant now = Instant.now();
        VerificationCode v;

        // Pick identifier: email OR phone
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            String rawEmail = request.getEmail().trim().toLowerCase();
            v = verificationCodeRepository.findByEmail(rawEmail);
        } else {
            String rawPhone = request.getPhoneNumber().trim();
            v = verificationCodeRepository.findByPhoneNumber(rawPhone);
        }

        try {
            // No OTP or OTP mismatch
            if (v == null || !v.getOtp().equals(providedOtp)) {
                VerifyOTPFailResponse fail = new VerifyOTPFailResponse();
                fail.setNeedsSignup(false);
                return new ApiResponse<>(false, "Invalid OTP", fail);
            }

            // Expired OTP
            if (v.getExpiresAt() == null || v.getExpiresAt().isBefore(now)) {
                verificationCodeRepository.delete(v); // cleanup expired OTP
                VerifyOTPFailResponse fail = new VerifyOTPFailResponse();
                fail.setNeedsSignup(false);
                return new ApiResponse<>(false, "OTP expired", fail);
            }

            // OTP is valid → check if user or seller exists (by email or phone)
            Optional<User> userOpt;
            Optional<Seller> sellerOpt;

            if (v.getEmail() != null) {
                userOpt = userRepository.findByEmail(v.getEmail());
                sellerOpt = sellerRepository.findByEmail(v.getEmail());
            } else {
                userOpt = userRepository.findByPhoneNumber(v.getPhoneNumber());
                sellerOpt = sellerRepository.findByPhoneNumber(v.getPhoneNumber());
            }

            if (userOpt.isPresent() || sellerOpt.isPresent()) {
                UserRole role;
                String token;

                if (userOpt.isPresent()) {
                    User user = userOpt.get();
                    role = user.getRole(); // could be ROLE_CUSTOMER or ROLE_ADMIN

                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            user.getEmail(),
                            null,
                            List.of(new SimpleGrantedAuthority(role.name()))
                    );
                    token = jwtProvider.generateToken(authentication);

                } else {
                    Seller seller = sellerOpt.get();
                    role = UserRole.ROLE_SELLER;

                    Authentication auth = new UsernamePasswordAuthenticationToken(
                            seller.getEmail(),
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_SELLER"))
                    );
                    token = jwtProvider.generateToken(auth);
                }

                // Success response
                VerifyOTPSuccessResponse success = new VerifyOTPSuccessResponse();
                success.setToken(token);
                success.setRole(role);

                // OTP is one-time → delete after successful verification
                verificationCodeRepository.delete(v);

                return new ApiResponse<>(true, "Login successful", success);
            }

            // User not found → needs signup
            VerifyOTPFailResponse fail = new VerifyOTPFailResponse();
            fail.setNeedsSignup(true);

            verificationCodeRepository.delete(v); // OTP already used

            return new ApiResponse<>(false, "User not registered", fail);

        } catch (Exception ex) {
            return new ApiResponse<>(false, "Internal server error while verifying OTP", null);
        }
    }


    @Override
    @Transactional
    public ApiResponse<CustomerSignupResponse> signupCustomer(CustomerSignupRequest request) {
        if (request == null || request.getEmail() == null || request.getFullName() == null) {
            return new ApiResponse<>(false, "Full name and email are required", null);
        }

        String email = request.getEmail().trim().toLowerCase();

        // 1. Check if user already exists
        if (userRepository.findByEmail(email).isPresent()) {
            return new ApiResponse<>(false, "User already registered with this email", null);
        }

        try {
            // 2. Create new user
            User newUser = User.builder()
                    .fullName(request.getFullName())
                    .email(email)
                    .phoneNumber(request.getPhoneNumber())
                    .role(UserRole.ROLE_CUSTOMER)
                    .password(passwordEncoder.encode(UUID.randomUUID().toString())) // random temp password
                    .isEmailVerified(Boolean.TRUE)
                    .build();

            User savedUser = userRepository.save(newUser);

            // 3. Any Business requirement.

            // 4. Authenticate & generate JWT
            Authentication auth = new UsernamePasswordAuthenticationToken(
                    savedUser.getEmail(),
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER"))
            );
            SecurityContextHolder.getContext().setAuthentication(auth);

            String token = jwtProvider.generateToken(auth);

            // 5. Build response
            CustomerSignupResponse response = new CustomerSignupResponse();
            response.setMessage("Customer account created successfully");
            response.setToken(token);
            response.setRole(UserRole.ROLE_CUSTOMER);

            return new ApiResponse<>(true, "Customer account created successfully", response);

        } catch (Exception ex) {
            return new ApiResponse<>(false, "Error while signing up customer", null);
        }
    }

    @Override
    @Transactional
    public ApiResponse<SellerSignupResponse> signupSeller(SellerSignupRequest request) {
        if (request == null || request.getEmail() == null || request.getFullName() == null) {
            return new ApiResponse<>(false, "Full name and email are required", null);
        }

        String email = request.getEmail().trim().toLowerCase();

        // 1. Check if seller already exists
        if (sellerRepository.findByEmail(email).isPresent()) {
            return new ApiResponse<>(false, "Seller already registered with this email", null);
        }

        try {
            // 2. Create new seller entity
            Seller newSeller = Seller.builder()
                    .fullName(request.getFullName())
                    .email(email)
                    .phoneNumber(request.getPhoneNumber())
                    .role(UserRole.ROLE_SELLER)
                    .accountStatus(AccountStatus.ACTIVE)
                    .password(passwordEncoder.encode(UUID.randomUUID().toString())) // random temp password
                    .isEmailVerified(Boolean.TRUE)
                    .build();

            Seller savedSeller = sellerRepository.save(newSeller);

            // 3. Authenticate & generate JWT
            Authentication auth = new UsernamePasswordAuthenticationToken(
                    savedSeller.getEmail(),
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_SELLER"))
            );
            SecurityContextHolder.getContext().setAuthentication(auth);

            String token = jwtProvider.generateToken(auth);

            // 4. Build response
            SellerSignupResponse response = new SellerSignupResponse();
            response.setMessage("Seller account created successfully");
            response.setToken(token);
            response.setRole(UserRole.ROLE_SELLER);

            return new ApiResponse<>(true, "Seller account created successfully", response);

        } catch (Exception ex) {
            return new ApiResponse<>(false, "Error while signing up seller", null);
        }
    }

}
