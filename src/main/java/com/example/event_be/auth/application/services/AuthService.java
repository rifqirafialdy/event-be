package com.example.event_be.auth.application.services;

import com.example.event_be.auth.domain.valueObject.Token;
import com.example.event_be.auth.presentation.DTO.LoginRequestDTO;
import com.example.event_be.auth.presentation.DTO.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequestDTO request);
    Token refreshAccessToken(String refreshToken);
    void logout(String refreshToken);

}
