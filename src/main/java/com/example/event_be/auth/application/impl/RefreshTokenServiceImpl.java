package com.example.event_be.auth.application.impl;

import com.example.event_be.auth.application.services.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final StringRedisTemplate redisTemplate;
    private static final String PREFIX = "refresh_token:";

    @Override
    public void store(String jti, String userId, long expirationSeconds) {
        redisTemplate.opsForValue().set(PREFIX + jti, userId, expirationSeconds, TimeUnit.SECONDS);
    }

    @Override
    public boolean isValid(String jti) {

        return redisTemplate.hasKey(PREFIX + jti);
    }

    @Override
    public void revoke(String jti) {

        redisTemplate.delete(PREFIX + jti);
    }
}
