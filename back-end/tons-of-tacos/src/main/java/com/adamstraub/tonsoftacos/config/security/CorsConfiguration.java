package com.adamstraub.tonsoftacos.config.security;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration {
@Value("${origin}")
private String origin;
    @Bean
    public WebMvcConfigurer corsConfigurer(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NotNull CorsRegistry registry) {
             registry.addMapping("/**")
                     .allowedOrigins(origin) // alter in properies to suit needs
                     .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                     .allowedHeaders("*") // Adjust as needed
                     .allowCredentials(true);
             ;
            }
        };

    }
}
