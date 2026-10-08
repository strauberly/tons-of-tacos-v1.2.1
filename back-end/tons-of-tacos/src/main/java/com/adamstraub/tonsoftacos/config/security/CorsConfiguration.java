    package com.adamstraub.tonsoftacos.config.security;

    import lombok.extern.slf4j.Slf4j;
    import org.jetbrains.annotations.NotNull;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.web.servlet.config.annotation.CorsRegistry;
    import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
    @Slf4j
    @Configuration
    public class CorsConfiguration {
    @Value("${ORIGIN}")
    private String origin;
        @Bean
        public WebMvcConfigurer corsConfigurer(){
            log.info("Origin: {}", origin);
            return new WebMvcConfigurer() {
                @Override
                public void addCorsMappings(@NotNull CorsRegistry registry) {
    //                refine for each path not just one size does all
                 registry.addMapping("/**")
                         .allowedOrigins(origin) // alter in properies to suit needs
                         .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                         .allowedHeaders("*") // Adjust as needed
                         .allowCredentials(true);
                }
            };

        }
    }
