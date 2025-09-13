package org.astrobrains.authflow.service;

public interface EmailService {

    void sendVerificationOtpEmail(String userEmail, String otp, String subject, String text);
}
