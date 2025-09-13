package org.astrobrains.authflow.dto.request.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerSignupRequest {
    private String fullName;
    private String email;
    private String phoneNumber;

    // we have created a create seller API already.
    // we could use that api to create seller.
}
