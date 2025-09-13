package org.astrobrains.authflow;

import org.astrobrains.authflow.config.properties.CorsProperties;
import org.astrobrains.authflow.config.properties.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        JwtProperties.class,
        CorsProperties.class
})
public class AuthFlowApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthFlowApplication.class, args);
	}

}
