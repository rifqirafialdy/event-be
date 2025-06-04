package com.example.event_be.auth.infrastructure.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

public class CorsConfigurationImpl implements CorsConfigurationSource {

    @Override
    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // Allowed frontend origins
        corsConfiguration.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "http://localhost:3001",
                "http://0.0.0.0:3000",
                "http://host.docker.internal:3000",
                "https://purwafest.vercel.app"
        ));

        // Allowed HTTP methods
        corsConfiguration.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
        ));

        // Allowed request headers
        corsConfiguration.setAllowedHeaders(List.of(
                "Authorization", "Content-Type", "X-Requested-With"
        ));

        // Exposed response headers (if needed)
        corsConfiguration.setExposedHeaders(List.of(
                "Authorization"
                // Add "Set-Cookie" if you need to read it from frontend JS, usually unnecessary
        ));

        // Allow credentials (cookies, authorization headers, etc.)
        corsConfiguration.setAllowCredentials(true);

        // Cache pre-flight response for 1 hour
        corsConfiguration.setMaxAge(3600L);

        return corsConfiguration;
    }
}
