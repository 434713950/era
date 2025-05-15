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
import org.springframework.security.config.Customizer;
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

import java.text.ParseException;
import java.time.Instant;

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
            "-----BEGIN PRIVATE KEY-----" +
            "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDCJYCuldvXyMDl" +
            "nyk5Pd0ST9t3AnUeBrMB1Pu35bVbHDwmbz95M4c4rOiTVvnwJDalgphyVFw4lj5V" +
            "3GwBYwd+QUv68/VQE0L1C+dGIDHSXGWuO+wqvWVDedkDyB391v7R841BYfYjauEb" +
            "v4niaju79T/MDSuyOdbffqiN2NQ9vQ3oEYcR2zzPyhAIoooO7J3ChELuze2u5e6L" +
            "87NJDD4eD9E6miaqfHr+iRxcO5lxFu/NoCBLF6ufV5eWt0GtLtj6LFULCuXyHVn7" +
            "lpg0oz1mnZvAwuZwlplgRGUDYU7rvUMNQ9jpPb4rusZ4lV7auu/r6w5vByTG+OC1" +
            "FljUAxxjAgMBAAECggEAED1m2DO84f3u9MYtgpZEKcX4S6ou9cYFrz+LlsRmOSrR" +
            "dT2VB6RhyUHdaoW5/nZpA8s6yUV5OVqH+FKpzivkcpEvs9w4Rux1i13liJ4kzL3h" +
            "24MzrBePoudMWd6hc5xKUHGY/UEef7PvBI0vEXtlZ8GKBp2LbCseaC+GFedvSYKj" +
            "nJF5VH1OkVQJ4Hi91Ty37A0fctkAS0UJwrUHinNaOZH0/uEb8VkXe5sRUWjV/61g" +
            "B/us3D1esh0rgX8r2pf0BiYdpZVvmRvpbG7RFh6aV00Z9yurhilht3n8SYoyiR6D" +
            "a4DfLf4c7l2n21inKxDrcs3yOYSkqNbBty8sM+pqMQKBgQDhW9M/rF/9Zz0y9ZMF" +
            "oz1Av4wRWCZBOur7vnYoXjkzBCHPpELagBiWlHEGHGqCAgNPDh3ODwkdlNyGs/Yi" +
            "83O7DOzvAi/7kWxj3VMtLzg5lRUSVAG08X7I9s8yKJGspjyfJWaRmp/sSiK472Ej" +
            "Z4qCs2wv7lDQWWQenxIMiqQ+FQKBgQDci0I0z+x9BT3LNj4DuJ43td5YoTASncJ9" +
            "IomsMNSBlBV8jS7pbI9gUbEe6bKsAP3JZ9LKKOZZw1Gdmm8OBLzE6kCfTPvsOF3o" +
            "L5+3YDbI48WPG/vQXzPWuQur6znqZ9LN1woTtjnvjxjuA7duLWr2VvicN1r6yqGe" +
            "+L3/vnYGlwKBgQCDUKprs9nLItk6VHZzFoeerv7DLIY+BELgpj7bjuvtmj7Ja4G5" +
            "KGMrexvQ38YAM4QSELu1UnWRUyidJUgLXajWGdYF/1ROVpK+Lj1FWvp8My6wG1tT" +
            "QFUMbSSWqaUY4VT5tyVpOpxtZ1WMRZBovPCs8DfeRhO5FB2O6knuHOtPcQKBgGVJ" +
            "B2TdwMxB5fk3tg9bcD5BphWqITvLfBCgFf4ghtfjvGJxLIRDOS2RFvkNduMLqYIf" +
            "zmzNj8zVqNvqmuojPtZohrkiT+hSkr4ZcQ50f3SPNqHcxi8SKeqJHVUdOIHwoJ2s" +
            "DpKy87STUW2uA0X0UdVEZ/TGb2ASQ1uQ/SFAxU+NAoGAB7RrvSrxduXAIL1yWUso" +
            "P9JvuNQSFSqrwvwxD0Z27okiQRtH5Witgc03HRhv2Hw42eCElqpiUJaEwSwogORg" +
            "eoIb53tLStwhCrUbTKCHtvQZblVkrhHMxCeRdR02r2Lkh//64wsKO+tHT7Dv6RGr" +
            "BU2YdA/9kIE11pw8bp3gOro=" +
            "-----END PRIVATE KEY-----")
    private String privateKey;


    @Value("${era.jwt.publicKey:" +
            "-----BEGIN PUBLIC KEY-----" +
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwiWArpXb18jA5Z8pOT3d" +
            "Ek/bdwJ1HgazAdT7t+W1Wxw8Jm8/eTOHOKzok1b58CQ2pYKYclRcOJY+VdxsAWMH" +
            "fkFL+vP1UBNC9QvnRiAx0lxlrjvsKr1lQ3nZA8gd/db+0fONQWH2I2rhG7+J4mo7" +
            "u/U/zA0rsjnW336ojdjUPb0N6BGHEds8z8oQCKKKDuydwoRC7s3truXui/OzSQw+" +
            "Hg/ROpomqnx6/okcXDuZcRbvzaAgSxern1eXlrdBrS7Y+ixVCwrl8h1Z+5aYNKM9" +
            "Zp2bwMLmcJaZYERlA2FO671DDUPY6T2+K7rGeJVe2rrv6+sObwckxvjgtRZY1AMc" +
            "YwIDAQAB" +
            "-----END PUBLIC KEY-----}")
    private String publicKey;

    @Value("${era.token.expire:2592000}")
    private Integer tokenValiditySeconds;

    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcOperations jdbcOperations) {
        return new JdbcRegisteredClientRepository(jdbcOperations);
    }

    @Bean
    public OAuth2AuthorizationService authorizationService(RedisTemplate<String, Object> redisTemplate) {
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
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .formLogin(Customizer.withDefaults()); // 默认登录页
        return http.build();
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
    public JwtDecoder jwtDecoder() throws JOSEException, ParseException {
        RSAKey rsaKey = RSAKey.parse(this.publicKey);
        return NimbusJwtDecoder.withPublicKey(rsaKey.toRSAPublicKey()).build();
    }


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
