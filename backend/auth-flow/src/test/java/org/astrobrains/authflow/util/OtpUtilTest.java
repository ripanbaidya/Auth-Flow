package org.astrobrains.authflow.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;

import static org.junit.jupiter.api.Assertions.*;

class OtpUtilTest {

    @Test
    void generateOtp_shouldReturnSixDigitString() {
        // Act
        String otp = OtpUtil.generateOtp();
        
        // Assert
        assertEquals(6, otp.length(), "OTP should be exactly 6 digits long");
    }

    @Test
    void generateOtp_shouldContainOnlyDigits() {
        // Act
        String otp = OtpUtil.generateOtp();
        
        // Assert
        assertTrue(otp.matches("\\d{6}"), "OTP should contain only digits (0-9)");
    }

    @RepeatedTest(10)
    void generateOtp_shouldGenerateDifferentValues() {
        // This test is repeated to ensure randomness
        // Act
        String otp1 = OtpUtil.generateOtp();
        String otp2 = OtpUtil.generateOtp();
        
        // Assert - it's possible but extremely unlikely to get the same OTP twice
        assertNotEquals(otp1, otp2, "Consecutive OTPs should be different");
    }
}