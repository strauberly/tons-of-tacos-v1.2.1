package com.adamstraub.tonsoftacos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private final String baseUrl;
    private final String description;

    public OpenApiConfig(
            @Value("${app.api.dev-https-url}") String baseUrl,
            @Value("${app.api.description}") String description) {
        this.baseUrl = baseUrl;
        this.description = description;
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addServersItem(new Server().url(baseUrl).description(description));
    }
}
