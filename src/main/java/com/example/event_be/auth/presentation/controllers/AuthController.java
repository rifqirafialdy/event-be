package com.example.event_be.auth.presentation.controllers;

import com.example.event_be.auth.application.services.AuthService;
import com.example.event_be.auth.application.services.RefreshTokenService;
import com.example.event_be.auth.application.services.RegistrationService;
import com.example.event_be.auth.domain.entities.SysUser;
import com.example.event_be.auth.domain.entities.SysUserRole;
import com.example.event_be.auth.domain.valueObject.Token;
import com.example.event_be.auth.infrastructure.repositories.SysScreenModeAccessRepository;
import com.example.event_be.auth.infrastructure.repositories.SysUserRepository;
import com.example.event_be.auth.infrastructure.repositories.SysUserRoleRepository;
import com.example.event_be.auth.presentation.DTO.LoginRequestDTO;
import com.example.event_be.auth.presentation.DTO.LoginResponse;
import com.example.event_be.auth.presentation.DTO.RefreshTokenRequest;
import com.example.event_be.auth.presentation.DTO.Registration.RegisterUserRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegistrationService registrationService;
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final SysUserRepository sysUserRepository;
    private final SysUserRoleRepository sysUserRoleRepository;
    private final SysScreenModeAccessRepository screenModeAccessRepository


    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterUserRequest request) {
        registrationService.registerUser(request);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequestDTO request, HttpServletResponse response) {
        LoginResponse loginResponse = authService.login(request);

        long now = Instant.now().getEpochSecond();
        long accessExpiresIn = loginResponse.getAccessToken().getExpiresAt() - now;
        long refreshExpiresIn = loginResponse.getRefreshToken().getExpiresAt() - now;

        ResponseCookie accessCookie = ResponseCookie.from("accessToken", loginResponse.getAccessToken().getValue())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(accessExpiresIn)
                .sameSite("None")
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", loginResponse.getRefreshToken().getValue())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshExpiresIn)
                .sameSite("None")
                .build();

        response.addHeader("Set-Cookie", accessCookie.toString());
        response.addHeader("Set-Cookie", refreshCookie.toString());
        System.out.println("AccessToken Cookie: " + accessCookie.toString());
        System.out.println("RefreshToken Cookie: " + refreshCookie.toString());


        return ResponseEntity.ok(loginResponse);
    }



    @PostMapping("/refresh")
    public ResponseEntity<Token> refresh(@RequestBody RefreshTokenRequest request) {
        Token newAccessToken = authService.refreshAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(newAccessToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        // Get refresh token from cookie
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                }
            }
        }

        if (refreshToken != null) {
            authService.logout(refreshToken); // revoke from Redis etc.
        }

        // Clear cookies
        ResponseCookie accessCookie = ResponseCookie.from("accessToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("None")
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("None")
                .build();

        response.addHeader("Set-Cookie", accessCookie.toString());
        response.addHeader("Set-Cookie", refreshCookie.toString());

        return ResponseEntity.ok().build();
    }


    @GetMapping("/debug/refresh-token/{token}")
    public ResponseEntity<String> checkRefreshToken(@PathVariable String token) {
        boolean exists = refreshTokenService.isValid(token);
        return ResponseEntity.ok(exists ? "Token exists in Redis" : "Token NOT found");
    }
    @GetMapping("/users/me")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request, Authentication authentication) {
        String userId = authentication.getName();
        SysUser user = sysUserRepository.findById(userId).orElseThrow();

        List<SysUserRole> userRoles = sysUserRoleRepository.findBySysUserId(userId);
        List<String> roleCodes = userRoles.stream()
                .map(role -> role.getSysRole().getCode())
                .toList();

        // Fetch all screen-action access for the user's roles
        var accessList = screenModeAccessRepository
                .findBySysScreenRole_SysRole_CodeIn(roleCodes);

        // Group by screen and collect actions
        Map<String, List<String>> screenPermissions = accessList.stream()
                .collect(Collectors.groupingBy(
                        a -> a.getSysScreenRole().getSysScreen().getCode(),
                        Collectors.mapping(a -> a.getSysScreenAction().getSysAction().getCode(), Collectors.toList())
                ));

        // Map to frontend-friendly list
        List<Map<String, Object>> permissions = screenPermissions.entrySet().stream()
                .map(entry -> Map.of("screen", entry.getKey(), "actions", entry.getValue()))
                .toList();

        return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "email", user.getEmail(),
                "role", roleCodes.isEmpty() ? "UNKNOWN" : roleCodes.get(0),
                "permissions", permissions
        ));
    }



}
