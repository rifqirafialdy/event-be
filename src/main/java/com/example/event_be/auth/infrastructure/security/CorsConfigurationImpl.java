package com.example.event_be.auth.infrastructure.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

public class CorsConfigurationImpl implements CorsConfigurationSource {

    @Override
    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "http://localhost:3001",
                "http://0.0.0.0:3000",
                "http://host.docker.internal:3000",
                "https://purwafest.vercel.app"
        ));

        corsConfiguration.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
        ));

        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addExposedHeader("*");

        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setMaxAge(3600L);

        return corsConfiguration;
    }
}
