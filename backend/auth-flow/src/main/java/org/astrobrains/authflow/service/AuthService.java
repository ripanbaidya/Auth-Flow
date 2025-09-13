package org.astrobrains.authflow.service;


import org.astrobrains.authflow.dto.ApiResponse;
import org.astrobrains.authflow.dto.request.auth.CustomerSignupRequest;
import org.astrobrains.authflow.dto.request.auth.SellerSignupRequest;
import org.astrobrains.authflow.dto.request.otp.SendOTPRequest;
import org.astrobrains.authflow.dto.request.otp.VerifyOTPRequest;
import org.astrobrains.authflow.dto.response.auth.CustomerSignupResponse;
import org.astrobrains.authflow.dto.response.auth.SellerSignupResponse;
import org.astrobrains.authflow.dto.response.otp.SendOTPResponse;

public interface AuthService {

    /**
     * Sends an OTP to the user's email or phone number.
     *
     * @param request contains target user details (email/phone).
     * @return standardized API response containing OTP delivery status.
     */
    ApiResponse<SendOTPResponse> sendOTP(SendOTPRequest request);

    /**
     * Verifies the OTP provided by the user.
     *
     * @param request contains the OTP and user identifier.
     * @return standardized API response:
     *         - {@link org.astrobrains.authflow.dto.response.otp.VerifyOTPSuccessResponse} if verification is successful.
     *         - {@link org.astrobrains.authflow.dto.response.otp.VerifyOTPFailResponse} if verification fails.
     */
    ApiResponse<?> verifyOTP(VerifyOTPRequest request);

    /**
     * Creates a new customer account after OTP verification,
     * if no existing customer is found.
     *
     * @param request contains customer signup details.
     * @return standardized API response with customer signup result.
     */
    ApiResponse<CustomerSignupResponse> signupCustomer(CustomerSignupRequest request);

    /**
     * Creates a new seller account after OTP verification,
     * if no existing seller is found.
     *
     * @param request contains seller signup details.
     * @return standardized API response with seller signup result.
     */
    ApiResponse<SellerSignupResponse> signupSeller(SellerSignupRequest request);
}
