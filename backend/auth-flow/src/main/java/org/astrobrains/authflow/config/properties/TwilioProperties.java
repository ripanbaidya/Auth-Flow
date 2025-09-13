package org.astrobrains.authflow.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * This class is used to store the Twilio properties, like account-sid, auth-token, fromPhoneNumber and so on.
 * which are read from the application.properties file.
 */
@Getter @Setter
@Configuration
@ConfigurationProperties("twilio")
public class TwilioProperties {

    // Twilio account SID
    private String accountSid;

    // Twilio auth token
    private String authToken;

    // Twilio phone number
    private String phoneNumber;
}
