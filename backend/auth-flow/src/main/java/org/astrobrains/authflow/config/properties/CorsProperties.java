package org.astrobrains.authflow.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter @Setter
@ConfigurationProperties(prefix = "application.cors")
public class CorsProperties {

    /**
     * List of all allowed origins.
     */
    private List<String> allowedOrigins;
}
