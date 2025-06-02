package com.example.event_be.auth.application.services;

import com.example.event_be.auth.presentation.DTO.Registration.RegisterUserRequest;

public interface RegistrationService {
    void registerUser(RegisterUserRequest request);
}
