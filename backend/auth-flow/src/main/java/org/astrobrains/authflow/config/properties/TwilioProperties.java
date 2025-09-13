package org.astrobrains.authflow.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * This class is used to store the Twilio properties, like account-sid, auth-token, fromPhoneNumber and so on.
 * which are read from the application.properties file.
 */
@Data
@Configuration
@ConfigurationProperties("twilio")
public class TwilioProperties {

    private String accountSid;

    private String authToken;

    private String phoneNumber;
}
