/*
 * Copyright (c) 2025. created by ourexists.https://gitee.com/ourexists
 */

package com.ourexists.era.framework.webserver.authorization;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;

import java.time.Duration;
import java.time.Instant;

@Slf4j
public class RedisOAuth2AuthorizationService implements OAuth2AuthorizationService {

    private static final String AUTH_PREFIX = "era:oauth2:auth:";

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisOAuth2AuthorizationService(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void save(OAuth2Authorization authorization) {
        String key = buildKey(authorization.getId());
        redisTemplate.opsForValue().set(key, authorization, getTtl(authorization));
        cacheTokens(authorization);
    }

    @Override
    public void remove(OAuth2Authorization authorization) {
        redisTemplate.delete(buildKey(authorization.getId()));
        deleteTokens(authorization);
    }

    @Override
    public OAuth2Authorization findById(String id) {
        Object obj = redisTemplate.opsForValue().get(buildKey(id));
        return (obj instanceof OAuth2Authorization) ? (OAuth2Authorization) obj : null;
    }

    @Override
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        if (token == null) {
            return null;
        }
        String tokenKey = buildTokenKey(token);
        String id = (String) redisTemplate.opsForValue().get(tokenKey);
        return id != null ? findById(id) : null;
    }


    private String buildKey(String id) {
        return AUTH_PREFIX + "id:" + id;
    }

    private String buildTokenKey(String token) {
        return AUTH_PREFIX + "token:" + token;
    }

    private void cacheTokens(OAuth2Authorization authorization) {
        if (authorization.getAccessToken() != null) {
            String tokenValue = authorization.getAccessToken().getToken().getTokenValue();
            redisTemplate.opsForValue().set(buildTokenKey(tokenValue), authorization.getId(), getTtl(authorization));
        }

        if (authorization.getRefreshToken() != null) {
            String tokenValue = authorization.getRefreshToken().getToken().getTokenValue();
            redisTemplate.opsForValue().set(buildTokenKey(tokenValue), authorization.getId(), getTtl(authorization));
        }
    }

    // 删除 token 映射
    private void deleteTokens(OAuth2Authorization authorization) {
        if (authorization.getAccessToken() != null) {
            redisTemplate.delete(buildTokenKey(authorization.getAccessToken().getToken().getTokenValue()));
        }

        if (authorization.getRefreshToken() != null) {
            redisTemplate.delete(buildTokenKey(authorization.getRefreshToken().getToken().getTokenValue()));
        }
    }

    // 设置缓存过期时间（TTL）
    private Duration getTtl(OAuth2Authorization authorization) {
        Instant expiresAt = null;
        if (authorization.getAccessToken() != null) {
            expiresAt = authorization.getAccessToken().getToken().getExpiresAt();
        } else if (authorization.getRefreshToken() != null) {
            expiresAt = authorization.getRefreshToken().getToken().getExpiresAt();
        }

        if (expiresAt != null) {
            return Duration.between(Instant.now(), expiresAt);
        }
        return Duration.ofHours(5);
    }
}
