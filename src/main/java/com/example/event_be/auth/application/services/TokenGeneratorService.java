package com.example.event_be.auth.application.services;


import com.example.event_be.auth.domain.valueObject.Token;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

public interface TokenGeneratorService {
    Token generateAccessToken(String userId, String email, String role);
    Token generateRefreshToken(String userId,String email,String role);
    Token generateAccessToken(String refreshToken);
    boolean isRefreshToken(String token);
    Jwt decodeRefreshToken(String token);


}
