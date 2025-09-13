package org.astrobrains.authflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * This class represents the response from the verify OTP API.
 * It contains either an existing user or a newly created user.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T>{
    /**
     * If user already exist then success would be true,
     * otherwise it would return false.
     */
    private Boolean success;
    private String message;

    /**
     * This field represents the response from the verify OTP API. It can be either a success
     * response or a failure response.
     * If the user already exists, the success field will be set to true, otherwise it will be
     * set to false.
     *
     * The specific response type will depend on the value of the success field. If the success
     * field is true, the response will be of type VerifyOTPSuccessResponse, which contains the
     * necessary data for the user.
     * If the success field is false, the response will be of type VerifyOTPFailResponse, which
     * indicates that the user does not exist and needs to sign up.
     */
    private T data; // generic payload

}
