package org.astrobrains.authflow.service;

public interface TwilioSmsService {

    void sendOtpSms(String phoneNumber, String otp);
}
