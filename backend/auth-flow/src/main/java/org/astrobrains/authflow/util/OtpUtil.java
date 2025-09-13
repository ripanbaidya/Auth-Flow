package org.astrobrains.authflow.util;

import java.security.SecureRandom;

public class OtpUtil {

    /**
     * This method will generate a random OTP of length 6. according to our need,
     * we can change the length of OTP.
     */
    public static String generateOtp() {
        int otpLength = 6;
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder();

        for (int i = 0; i < otpLength; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }
}
