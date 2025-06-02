package com.example.event_be.auth.presentation.DTO.Registration;

import lombok.Data;

@Data
public class RegisterUserRequest {
    private String name;
    private String email;
    private String password;
    private String role; // "CUSTOMER" or "ORGANIZER"
    private String referredBy; // referral code (optional)
}
