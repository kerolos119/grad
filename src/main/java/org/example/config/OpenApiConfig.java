package org.example.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Eyes On Plants API")
                        .description("E-commerce API for plant selling platform with order management, payments, and user authentication")
                        .contact(new Contact()
                                .name("Support")
                                .email("support@eyesonplants.com")
                                .url("https://www.eyesonplants.com/support"))
                        .license(new License()
                                .name("Private License")
                                .url("https://www.eyesonplants.com/license"))
                )
                .servers(List.of(
                        new Server().url("http://localhost:" + serverPort).description("Development Server"),
                        new Server().url("https://api.eyesonplants.com").description("Production Server")
                ))
                .components(new Components()
                        .addSecuritySchemes("bearer-jwt", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")
                                .description("JWT Authorization header using the Bearer scheme")
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList("bearer-jwt"));
    }
}