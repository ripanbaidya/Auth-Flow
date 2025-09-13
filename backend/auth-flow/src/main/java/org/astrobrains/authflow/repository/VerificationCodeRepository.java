package org.astrobrains.authflow.repository;

import org.astrobrains.authflow.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {

    VerificationCode findByEmail(String mail);

    VerificationCode findByPhoneNumber(String phoneNumber);

    VerificationCode findByOtp(String otp);

    // optional: find only non-expired
    VerificationCode findByEmailAndExpiresAtAfter(String email, Instant now);

    // cleanup helper
    void deleteAllByExpiresAtBefore(Instant now);
}
