package com.example.event_be.auth.application.impl;

import com.example.event_be.auth.application.services.AuthService;
import com.example.event_be.auth.application.services.RefreshTokenService;
import com.example.event_be.auth.application.services.TokenGeneratorService;
import com.example.event_be.auth.domain.entities.SysUser;
import com.example.event_be.auth.domain.valueObject.Token;
import com.example.event_be.auth.infrastructure.repositories.SysUserRepository;
import com.example.event_be.auth.presentation.DTO.LoginRequestDTO;
import com.example.event_be.auth.presentation.DTO.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final SysUserRepository sysUserRepository;
    private final TokenGeneratorService tokenGeneratorService;
    private final RefreshTokenService refreshTokenService;


    @Override
    public LoginResponse login(LoginRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        SysUser user = sysUserRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String role = user.getUserRoles()
                .stream()
                .findFirst()
                .map(userRole -> userRole.getSysRole().getCode())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        Token accessToken = tokenGeneratorService.generateAccessToken(user.getId(), user.getEmail(), role);
        Token refreshToken = tokenGeneratorService.generateRefreshToken(user.getId(), user.getEmail(), role);

        refreshTokenService.store(refreshToken.getValue(), user.getId(), refreshToken.getExpiresAt());


        return LoginResponse.builder()
                .email(user.getEmail())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public Token refreshAccessToken(String refreshToken) {
        System.out.println("🔁 Attempting to refresh token...");
        System.out.println("Token: " + refreshToken);

        if (!refreshTokenService.isValid(refreshToken)) {
            System.out.println("❌ Token not found in Redis");
            throw new RuntimeException("Refresh token is revoked or reused");
        }

        Jwt jwt = tokenGeneratorService.decodeRefreshToken(refreshToken);
        System.out.println("✅ Decoded JWT: " + jwt.getClaims());

        if (jwt.getExpiresAt().isBefore(Instant.now())) {
            System.out.println("❌ Token expired");
            throw new RuntimeException("Refresh token expired");
        }

        String userId = jwt.getSubject();
        String email = jwt.getClaimAsString("email");
        String role = jwt.getClaimAsString("scope");

        return tokenGeneratorService.generateAccessToken(userId, email, role);
    }


    @Override
    public void logout(String refreshToken) {
        if (!tokenGeneratorService.isRefreshToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        if (!refreshTokenService.isValid(refreshToken)) {
            throw new RuntimeException("Refresh token already revoked or not found");
        }

        refreshTokenService.revoke(refreshToken);
    }


}
