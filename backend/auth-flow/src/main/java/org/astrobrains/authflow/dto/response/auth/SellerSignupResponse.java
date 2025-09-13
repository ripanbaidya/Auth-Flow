package org.astrobrains.authflow.dto.response.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.astrobrains.authflow.enums.UserRole;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerSignupResponse {
    private String message;
    private String token;

    // we are not using refresh token
    // private String refreshToken;

    private UserRole role;
}
