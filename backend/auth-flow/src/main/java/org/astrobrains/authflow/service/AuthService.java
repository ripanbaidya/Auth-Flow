package org.astrobrains.authflow.service;


import org.astrobrains.authflow.response.ApiResponse;
import org.astrobrains.authflow.request.auth.CustomerSignupRequest;
import org.astrobrains.authflow.request.auth.SellerSignupRequest;
import org.astrobrains.authflow.request.otp.SendOTPRequest;
import org.astrobrains.authflow.request.otp.VerifyOTPRequest;
import org.astrobrains.authflow.response.auth.CustomerSignupResponse;
import org.astrobrains.authflow.response.auth.SellerSignupResponse;
import org.astrobrains.authflow.response.otp.SendOTPResponse;
import org.astrobrains.authflow.response.otp.VerifyOTPFailResponse;
import org.astrobrains.authflow.response.otp.VerifyOTPSuccessResponse;

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
     *         - {@link VerifyOTPSuccessResponse} if verification is successful.
     *         - {@link VerifyOTPFailResponse} if verification fails.
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
