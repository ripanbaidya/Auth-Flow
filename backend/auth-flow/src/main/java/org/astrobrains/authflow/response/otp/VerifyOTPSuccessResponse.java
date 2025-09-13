package org.astrobrains.authflow.response.otp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.astrobrains.authflow.enums.Role;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerifyOTPSuccessResponse {
    private String token;
    private Role role;
}
