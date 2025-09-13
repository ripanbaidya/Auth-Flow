package org.astrobrains.authflow.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    /**
     * This method returns an OpenAPI object that is used to generate the API documentation.
     * @return OpenAPI
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Auth-Flow API")
                        .version("v1")
                        .description("API Documentation for Auth-Flow Application")
                        .contact(new Contact()
                                .name("Ripan Baidya")
                                .name("ripan.baidya024@gmail.com")
                        )
                );
    }
}
