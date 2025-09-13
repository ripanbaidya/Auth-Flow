package org.astrobrains.authflow.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "application.cors")
public class CorsProperties {

    // List of allowed origins
    private List<String> allowedOrigins;
}
