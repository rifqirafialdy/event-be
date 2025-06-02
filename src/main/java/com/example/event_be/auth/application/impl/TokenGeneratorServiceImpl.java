package com.example.event_be.auth.application.impl;

import com.example.event_be.auth.application.services.TokenGeneratorService;
import com.example.event_be.auth.domain.enums.TokenType;
import com.example.event_be.auth.domain.valueObject.Token;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class TokenGeneratorServiceImpl implements TokenGeneratorService {

    private final JwtEncoder accessTokenEncoder;
    private final JwtEncoder refreshTokenEncoder;
    private final JwtDecoder refreshTokenDecoder;
    private final JwtDecoder jwtDecoder;

    public TokenGeneratorServiceImpl(
            @Qualifier("jwtEncoder") JwtEncoder accessTokenEncoder,
            @Qualifier("refreshTokenEncoder") JwtEncoder refreshTokenEncoder,
            @Qualifier("refreshTokenDecoder") JwtDecoder refreshTokenDecoder,
            @Qualifier("jwtDecoder") JwtDecoder jwtDecoder
    ) {
        this.accessTokenEncoder = accessTokenEncoder;
        this.refreshTokenEncoder = refreshTokenEncoder;
        this.refreshTokenDecoder = refreshTokenDecoder;
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    public Token generateAccessToken(String userId, String email, String role) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(now.plusSeconds(3600))
                .subject(userId) // ✅ Use user ID
                .claim("email", email) // Optional for frontend or audit
                .claim("scope", role)
                .claim("kind", TokenType.ACCESS.getType())
                .build();
        JwsHeader header = JwsHeader.with(() -> "HS256").build();

        return new Token(
                accessTokenEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue(),
                3600,
                "Bearer"
        );
    }

    @Override
    public Token generateRefreshToken(String userId, String email, String role) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(now.plus(30, ChronoUnit.DAYS))
                .subject(userId)
                .claim("email", email)  // ✅ Add this
                .claim("scope", role)   // ✅ Add this
                .claim("kind", TokenType.REFRESH.getType())
                .build();

        JwsHeader header = JwsHeader.with(() -> "HS256").build();

        return new Token(
                refreshTokenEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue(),
                (int) ChronoUnit.SECONDS.between(now, now.plus(30, ChronoUnit.DAYS)),
                "Bearer"
        );
    }



    @Override
    public Token generateAccessToken(String refreshToken) {
        Jwt jwt = refreshTokenDecoder.decode(refreshToken);
        if (jwt.getExpiresAt().isBefore(Instant.now())) {
            throw new IllegalArgumentException("Refresh token expired");
        }

        String userId = jwt.getSubject();
        String scope = jwt.getClaimAsString("scope");
        String email = jwt.getClaimAsString("email");

        return generateAccessToken(userId, email, scope);
    }

    @Override
    public boolean isRefreshToken(String token) {
        Jwt jwt = refreshTokenDecoder.decode(token);
        return TokenType.REFRESH.getType().equals(jwt.getClaimAsString("kind"));
    }

    @Override
    public Jwt decodeRefreshToken(String token) {
        return refreshTokenDecoder.decode(token);
    }

}
