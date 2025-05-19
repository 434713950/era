/*
 * Copyright (C) 2025  ChengPeng
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package com.ourexists.era.oauth2.authorization;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.ourexists.era.framework.core.PathRule;
import com.ourexists.era.oauth2.core.EraPasswordEncoder;
import com.ourexists.era.oauth2.core.handler.EmptyEraAccessDeniedHandler;
import com.ourexists.era.oauth2.core.handler.EmptyEraAuthenticationEntryPoint;
import com.ourexists.era.oauth2.core.handler.EraAccessDeniedHandler;
import com.ourexists.era.oauth2.core.handler.EraAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;

/**
 * 认证服务配置
 *
 * @author PengCheng
 * @date 2018/8/28
 */
@Configuration
@Order(Integer.MIN_VALUE)
public class AuthorizationServerConfigurer {

    @Value("${era.jwt.privateKey:" +
            "-----BEGIN PRIVATE KEY-----\n" +
            "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDc7MhMTDRJMFgj\n" +
            "Ldsxg5iqJbxg5b/Dq7K527mFOtMvSuYQ9rEoVKOWaGD33kvKJw8vL8fO9L9sziY4\n" +
            "+0/mOX8AI63uL69oC4hAZQ07RRMcn7/ZWkbEmwzGC6qxA0GymXy1mWTHQmR5WWWo\n" +
            "SMAUqfEuyvunOpTMDGOPOc7mbeTuK637gwqTmfqn+/gEnPLNH4fHqjPGfv8mQNxd\n" +
            "izqxGWpNCCm9EcmwcdxezHXfkiHxCG6gZnoX6bDfH7361atvhye5iuKwzDR6jEOM\n" +
            "7PTiUvcyHkVQtriIUmtlTBXiUQ+X72TYW2GGrpNQ1+gyyc1woIR4MsK5Alk3LVJT\n" +
            "BclhXIB5AgMBAAECggEARkWS2KIMp5MXFemKJU/EiaqDJxctTiIZMYEwm65AanSa\n" +
            "hZ7Sbw+tETSWU4GD/gM6kznRhoTXNtbD1bAqlAKJ4RN4SaElXCT+6+y4rrekAqDV\n" +
            "t189z1+6Smx/mNp+VT75KhJmufLmXqsO6N5rKqd7rfSm42SA2/681FwA8c7W2OK4\n" +
            "oP31LsgY1OsGvFqiOrgG0qNqWy8rJzVLcpd4czA0FrKuKyhM38AVerSoIp03Gc3G\n" +
            "x/VwPkrJwzKzkmjibU/t3uURUOtpSGhG/VoJ+bKMDYEpsPaKRF63J+i5kU+PXk/t\n" +
            "UYUfmnuWWDjstu0mQEO2Hdj11XMlGpn6OZhHCIDU+QKBgQDwxJnicIQ537O3zOHM\n" +
            "WZL0knANxxJ0qUDGQtEAjl1UkjHhz5a+auPpu1ZP5JgpXnhDUNXWnrhbwU+hIEm6\n" +
            "hTRb9KXqJbRxHmI9vt3nxt//JVQbgkzytn8ymiutzEQ/+1tMNH0gwhP6gqmpe5mC\n" +
            "s4fduo9v3GLTAQGg4SzBSYJbOwKBgQDq5s9XSQQfejRPv3aX1UyMyhB0wfpggkA7\n" +
            "VyLrCnwjAZzSbfQtx4UXOY1yTPHYQx80pbKtf2TcZiO70ucNZ1wEM0jujBD7VkAV\n" +
            "/sZ7RE/yq6LArvKWW0G6WG2I5jc+gaTHGqolKorRizYqrPLGzcYShSirbIn6N3WJ\n" +
            "wmCZ2oAP2wKBgGiT3Itgd+5zXiptkX4jQhN9L7KsTzXg9kOnbgSh0aQURBpjAoT/\n" +
            "BLPXLSxSjE0bvXzvtZIdKtKf6qh/z8Z2aUGvyAkmC3Q+0EkliFlOJqk6W4f/VtDt\n" +
            "t94Q3PwGh3aLBSLagaci5W6gJnV5PDprJI6IpBjgTwR7oWtxovDprvdVAoGAevgB\n" +
            "KuxqYCAVKnpMlwduX3WYT8cMT2FgRrBC81A11A2QDwjIfv6nyZSzW1a0dEYPG/xy\n" +
            "ISlDn61In5a0peup2/kNAPQKH8jzG5CYTwdf4uW++aecDuIO8oJANR5vZSVxIVnw\n" +
            "ICy5JyD9ZjHLlg861Y8nzzWutXI68bbz4xbjW2kCgYEAjDsUhCiDHjlMAwKs6sTD\n" +
            "S1ZUYTvC91tG/MBjj93skn1peBvakHZQKlDfksbxyX9uYp5xRz7N/iGQ4FiyHleT\n" +
            "cI71eVABbSGf5qPE00DRbICjwlUIZyDJrKgIAXk1ErF7hfaDoJvnJEnTHpyB9q9P\n" +
            "frBu+ZvvK+e4NyDOVwk+trY=\n" +
            "-----END PRIVATE KEY-----\n" +
            "}")
    private String privateKey;


    @Value("${era.jwt.publicKey:" +
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA3OzITEw0STBYIy3bMYOY" +
            "qiW8YOW/w6uyudu5hTrTL0rmEPaxKFSjlmhg995LyicPLy/HzvS/bM4mOPtP5jl/" +
            "ACOt7i+vaAuIQGUNO0UTHJ+/2VpGxJsMxguqsQNBspl8tZlkx0JkeVllqEjAFKnx" +
            "Lsr7pzqUzAxjjznO5m3k7iut+4MKk5n6p/v4BJzyzR+Hx6ozxn7/JkDcXYs6sRlq" +
            "TQgpvRHJsHHcXsx135Ih8QhuoGZ6F+mw3x+9+tWrb4cnuYrisMw0eoxDjOz04lL3" +
            "Mh5FULa4iFJrZUwV4lEPl+9k2Fthhq6TUNfoMsnNcKCEeDLCuQJZNy1SUwXJYVyA" +
            "eQIDAQAB" +
            "}")
    private String publicKey;

    @Value("${era.token.expire:2592000}")
    private Integer tokenValiditySeconds;

    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcOperations jdbcOperations) {
        return new JdbcRegisteredClientRepository(jdbcOperations);
    }

    @Bean
    public OAuth2AuthorizationService authorizationService(RedisTemplate redisTemplate) {
        return new RedisOAuth2AuthorizationService(redisTemplate);
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
        return context -> {
            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
                Instant now = Instant.now();
                context.getClaims().claim("copyright", "ourexists"); // 你也可以加其他字段
                context.getClaims().issuedAt(now);
                context.getClaims().expiresAt(now.plusSeconds(tokenValiditySeconds));
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new EraPasswordEncoder();
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() throws JOSEException {
        JWK jwk = RSAKey.parseFromPEMEncodedObjects(this.privateKey);
        JWKSet jwkSet = new JWKSet(jwk);
        return (jwkSelector, context) -> jwkSelector.select(jwkSet);
    }

    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public JwtDecoder jwtDecoder() throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] encoded = Base64.getDecoder().decode(this.publicKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        return NimbusJwtDecoder.withPublicKey( (RSAPublicKey) keyFactory.generatePublic(keySpec)).build();
    }

//    @Bean
//    public SecurityFilterChain eraAuthorizationSecurityFilterChain(HttpSecurity http,
//                                                          EraAuthenticationEntryPoint eraAuthenticationEntryPoint) throws Exception {
//        http
//                .securityMatcher(PathRule.OAUTH_PATHS)
//                .exceptionHandling(exception -> exception
//                        .authenticationEntryPoint(eraAuthenticationEntryPoint)
//                );
//        return http.build();
//    }

    @Bean
    @ConditionalOnMissingBean(EraAuthenticationEntryPoint.class)
    public EraAuthenticationEntryPoint eraAuthenticationEntryPoint() {
        return new EmptyEraAuthenticationEntryPoint();
    }

    @Bean
    @ConditionalOnMissingBean(EraAccessDeniedHandler.class)
    public EraAccessDeniedHandler eraAccessDeniedHandler() {
        return new EmptyEraAccessDeniedHandler();
    }
}
