package org.astrobrains.authflow.dto.response.otp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerifyOTPFailResponse {
    /**
     * If the user does not exist, this field will be true. and user needs to signup
     * user can be customer/ seller.
     */
    private Boolean needsSignup;

}
