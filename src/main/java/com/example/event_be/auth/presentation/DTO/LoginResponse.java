package com.example.event_be.auth.presentation.DTO;

import com.example.event_be.auth.domain.valueObject.Token;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private String email;
    private Token accessToken;
    private Token refreshToken;
}
