package com.zerobase.zerobasereservation.reservation.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// url : http://localhost:8080/swagger-ui/index.html#/
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            // Adds general API metadata
            .info(new Info()
                // Title displayed in Swagger UI
                .title("My API")
                // Short description about the API
                .description("API documentation for My Application")
                // Version of the API
                .version("1.0.0")
                // Contact information for API support or details
                .contact(new Contact()
                    .name("API Support")
                    .email("support@example.com")
                    .url("https://example.com")))
            ;
    }
}
