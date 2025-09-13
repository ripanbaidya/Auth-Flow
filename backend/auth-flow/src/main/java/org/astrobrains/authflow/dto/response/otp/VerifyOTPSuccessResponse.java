package org.astrobrains.authflow.dto.response.otp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.astrobrains.authflow.enums.UserRole;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerifyOTPSuccessResponse {
    private String token;
    private UserRole role;
}
