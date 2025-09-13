package org.astrobrains.authflow.response.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.astrobrains.authflow.enums.Role;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSignupResponse {
    private String message;
    private String token;

    // we are not using refresh token
    // private String refreshToken;

    private Role role;
}
