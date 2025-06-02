package com.example.event_be.auth.presentation.controllers;

import com.example.event_be.auth.application.services.AuthService;
import com.example.event_be.auth.application.services.RefreshTokenService;
import com.example.event_be.auth.application.services.RegistrationService;
import com.example.event_be.auth.domain.entities.SysUser;
import com.example.event_be.auth.domain.valueObject.Token;
import com.example.event_be.auth.infrastructure.repositories.SysUserRepository;
import com.example.event_be.auth.infrastructure.repositories.SysUserRoleRepository;
import com.example.event_be.auth.presentation.DTO.LoginRequestDTO;
import com.example.event_be.auth.presentation.DTO.LoginResponse;
import com.example.event_be.auth.presentation.DTO.RefreshTokenRequest;
import com.example.event_be.auth.presentation.DTO.Registration.RegisterUserRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegistrationService registrationService;
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final SysUserRepository sysUserRepository;
    private final SysUserRoleRepository sysUserRoleRepository;


    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterUserRequest request) {
        registrationService.registerUser(request);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequestDTO request, HttpServletResponse response) {
        LoginResponse loginResponse = authService.login(request);

        // Access Token Cookie
        ResponseCookie accessCookie = ResponseCookie.from("accessToken", loginResponse.getAccessToken().getValue())
                .httpOnly(true)
                .secure(true) // ✅ Set to false only for local dev without HTTPS
                .path("/")
                .maxAge(loginResponse.getAccessToken().getExpiresAt())
                .sameSite("Strict")
                .build();

        // Refresh Token Cookie
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", loginResponse.getRefreshToken().getValue())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(loginResponse.getRefreshToken().getExpiresAt())
                .sameSite("Strict")
                .build();

        // Add both cookies as separate headers
        return ResponseEntity.ok()
                .header("Set-Cookie", accessCookie.toString())
                .header("Set-Cookie", refreshCookie.toString())
                .body(loginResponse);
    }


    @PostMapping("/refresh")
    public ResponseEntity<Token> refresh(@RequestBody RefreshTokenRequest request) {
        Token newAccessToken = authService.refreshAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(newAccessToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody RefreshTokenRequest request) {
        authService.logout(request.getRefreshToken());
        return ResponseEntity.ok().build();
    }
    @GetMapping("/debug/refresh-token/{token}")
    public ResponseEntity<String> checkRefreshToken(@PathVariable String token) {
        boolean exists = refreshTokenService.isValid(token);
        return ResponseEntity.ok(exists ? "Token exists in Redis" : "Token NOT found");
    }
    @GetMapping("/users/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        String userId = authentication.getName();
        SysUser user = sysUserRepository.findById(userId).orElseThrow();

        String role = sysUserRoleRepository.findBySysUserId(userId)
                .stream()
                .findFirst()
                .map(r -> r.getSysRole().getCode())
                .orElse("UNKNOWN");

        return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "email", user.getEmail(),
                "role", role
        ));
    }



}
