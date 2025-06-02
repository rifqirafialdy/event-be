package com.example.event_be.auth.infrastructure.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

public class CorsConfigurationImpl implements CorsConfigurationSource {

    @Override
    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // ✅ Specific allowed origins
        corsConfiguration.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "http://localhost:3001",
                "http://0.0.0.0:3000",
                "http://host.docker.internal:3000",
                "https://purwafest.vercel.app"
        ));

        // ✅ Allow necessary HTTP methods
        corsConfiguration.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
        ));

        // ✅ Allow required headers
        corsConfiguration.setAllowedHeaders(List.of(
                "Authorization", "Cache-Control", "Content-Type"
        ));

        // ✅ Expose any headers the frontend needs to read (optional)
        corsConfiguration.setExposedHeaders(List.of(
                "Authorization"
        ));

        // ✅ Allow cookies
        corsConfiguration.setAllowCredentials(true);

        return corsConfiguration;
    }
}
