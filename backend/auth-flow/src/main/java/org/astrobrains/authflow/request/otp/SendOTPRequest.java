package org.astrobrains.authflow.request.otp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendOTPRequest {
    private String email;
    private String phoneNumber;
}
