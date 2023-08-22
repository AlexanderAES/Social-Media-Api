package com.socialmediaapi.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SecurityScheme(
        name = "JWT",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer")
@Configuration
public class SwaggerConfig {

    @Value("${swagger.app.title}")
    private String title;
    @Value("${swagger.version}")
    private String version;
    @Value("${swagger.contact.email}")
    private String email;
    @Value("${swagger.contact.name}")
    private String name;

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI().info(
                new Info()
                        .title(title)
                        .version(version)
                        .contact(
                                new Contact()
                                        .email(email)
                                        .name(name)));
    }
}
