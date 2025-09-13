package org.astrobrains.authflow.controller;

import lombok.RequiredArgsConstructor;
import org.astrobrains.authflow.dto.ApiResponse;
import org.astrobrains.authflow.dto.request.auth.CustomerSignupRequest;
import org.astrobrains.authflow.dto.request.auth.SellerSignupRequest;
import org.astrobrains.authflow.dto.request.otp.SendOTPRequest;
import org.astrobrains.authflow.dto.request.otp.VerifyOTPRequest;
import org.astrobrains.authflow.dto.response.auth.CustomerSignupResponse;
import org.astrobrains.authflow.dto.response.auth.SellerSignupResponse;
import org.astrobrains.authflow.dto.response.otp.SendOTPResponse;
import org.astrobrains.authflow.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponse<SendOTPResponse>> sendOtp(@RequestBody SendOTPRequest request) {
        ApiResponse<SendOTPResponse> response = authService.sendOTP(request);
        return ResponseEntity.status(response.getSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<?>> verifyOtp(@RequestBody VerifyOTPRequest request) {
        ApiResponse<?> response = authService.verifyOTP(request);
        return ResponseEntity.status(response.getSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @PostMapping("/signup/customer")
    public ResponseEntity<ApiResponse<CustomerSignupResponse>> signupCustomer(@RequestBody CustomerSignupRequest request) {
        ApiResponse<CustomerSignupResponse> response = authService.signupCustomer(request);
        return ResponseEntity.status(response.getSuccess() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @PostMapping("/signup/seller")
    public ResponseEntity<ApiResponse<SellerSignupResponse>> signupSeller(@RequestBody SellerSignupRequest request) {
        ApiResponse<SellerSignupResponse> response = authService.signupSeller(request);
        return ResponseEntity.status(response.getSuccess() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST)
                .body(response);
    }
}
