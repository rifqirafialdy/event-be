package com.example.event_be.auth.application.services;

public interface RefreshTokenService {
    void store(String jti, String userId, long expirationSeconds);
    boolean isValid(String jti);
    void revoke(String jti);
}
