package org.astrobrains.authflow.service;

public interface TwilioService {

    void sendOtpSms(String phoneNumber, String otp);
}
