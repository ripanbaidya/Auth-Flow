package org.astrobrains.authflow.dto.request.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSignupRequest {
    private String fullName;
    private String email;
    private String phoneNumber;
}
