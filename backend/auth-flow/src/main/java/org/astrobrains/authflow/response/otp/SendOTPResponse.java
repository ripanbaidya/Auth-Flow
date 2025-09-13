package org.astrobrains.authflow.dto.response.otp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendOTPResponse {
    private String message;
    private Integer expiresIn; // in seconds
}
