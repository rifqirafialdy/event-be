package com.example.event_be.auth.presentation.controllers;

import com.example.event_be.auth.application.services.AuthService;
import com.example.event_be.auth.application.services.RefreshTokenService;
import com.example.event_be.auth.application.services.RegistrationService;
import com.example.event_be.auth.domain.entities.SysScreenModeAccess;
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
    private final SysScreenModeAccessRepository screenModeAccessRepository;


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
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                System.out.println("Cookie received: " + c.getName() + "=" + c.getValue());
            }
        } else {
            System.out.println("No cookies received.");
        }

        String userId = authentication.getName();
        SysUser user = sysUserRepository.findById(userId).orElseThrow();

        List<SysUserRole> roles = sysUserRoleRepository.findBySysUserId(userId);
        List<String> roleCodes = roles.stream()
                .map(r -> r.getSysRole().getCode())
                .toList();

        // fetch permissions for this user's role(s)
        List<SysScreenModeAccess> accesses = screenModeAccessRepository.findBySysScreenRole_SysRole_CodeIn(roleCodes);

        // build permission map: { screenCode: [actions] }
        Map<String, List<String>> permissions = accesses.stream()
                .collect(Collectors.groupingBy(
                        access -> access.getSysScreenRole().getSysScreen().getCode(),
                        Collectors.mapping(
                                access -> access.getSysScreenAction().getSysAction().getCode(),
                                Collectors.toList()
                        )
                ));

        // assume single role for simplicity, if needed you can return a list
        String role = roleCodes.isEmpty() ? "UNKNOWN" : roleCodes.get(0);

        return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "email", user.getEmail(),
                "role", role,
                "permissions", permissions
        ));
    }




}
