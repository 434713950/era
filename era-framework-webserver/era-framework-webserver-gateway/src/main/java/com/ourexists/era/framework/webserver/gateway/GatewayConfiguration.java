/*
 * Copyright (c) 2025. created by ourexists.https://gitee.com/ourexists
 */

package com.ourexists.era.framework.webserver.gateway;

import com.ourexists.era.framework.webserver.gateway.config.DefaultGatewayAccessDeniedHandler;
import com.ourexists.era.framework.webserver.gateway.config.DefaultGatewayAuthenticationEntryPoint;
import com.ourexists.era.framework.webserver.gateway.filter.EraGatewayFilterConfigurer;
import com.ourexists.era.framework.webserver.gateway.oauth2.RedisReactiveOAuth2AuthorizedClientService;
import com.ourexists.era.framework.webserver.gateway.ratelimiter.RateLimiterConfigurer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.endpoint.WebClientReactiveClientCredentialsTokenResponseClient;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @since 5.0
 */
@EnableWebFluxSecurity
@Configuration
@Import({RateLimiterConfigurer.class, EraGatewayFilterConfigurer.class})
class GatewayConfiguration {

    @Bean
    public ReactiveOAuth2AuthorizedClientService authorizedClientService(
            ReactiveRedisTemplate reactiveRedisTemplate,
            ReactiveClientRegistrationRepository clientRegistrationRepository) {
        return new RedisReactiveOAuth2AuthorizedClientService(reactiveRedisTemplate, clientRegistrationRepository);
    }

    @Bean
    public ReactiveOAuth2AuthorizedClientManager authorizedClientManager(
            ReactiveClientRegistrationRepository registrations,
            ReactiveOAuth2AuthorizedClientService clientService,
            @Qualifier("loadBalancedWebClientBuilder") WebClient.Builder webClientBuilder) {
        WebClientReactiveClientCredentialsTokenResponseClient tokenResponseClient =
                new WebClientReactiveClientCredentialsTokenResponseClient();
        tokenResponseClient.setWebClient(webClientBuilder.build());
        ReactiveOAuth2AuthorizedClientProvider provider =
                ReactiveOAuth2AuthorizedClientProviderBuilder.builder()
                        .clientCredentials(builder -> builder.accessTokenResponseClient(tokenResponseClient))
                        .build();
        AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager manager =
                new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(registrations, clientService);
        manager.setAuthorizedClientProvider(provider);
        return manager;
    }

    @Bean
    public ServerAccessDeniedHandler serverAccessDeniedHandler() {
        return new DefaultGatewayAccessDeniedHandler();
    }

    @Bean
    public ServerAuthenticationEntryPoint serverAuthenticationEntryPoint() {
        return new DefaultGatewayAuthenticationEntryPoint();
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http,
                                                            ServerAccessDeniedHandler serverAccessDeniedHandler,
                                                            ServerAuthenticationEntryPoint serverAuthenticationEntryPoint) {
        http
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(serverAuthenticationEntryPoint)
                        .accessDeniedHandler(serverAccessDeniedHandler)
                )
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .anyExchange().permitAll() // 所有请求放行
                )
                .oauth2Client(Customizer.withDefaults());
        return http.build();
    }


    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }
}
