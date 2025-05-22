/*
 * Copyright (c) 2025. created by ourexists.https://gitee.com/ourexists
 */

package com.ourexists.era.framework.webserver.gateway.ratelimiter;

import com.ourexists.era.framework.core.EraSystemHeader;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

public class RateLimiterConfigurer {

    /**
     * ip地址限流
     * @return 限流key
     */
    @Bean
    @Primary
    public KeyResolver remoteAddressKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getHostName());
    }

    /**
     * 请求路径限流
     * @return 限流key
     */
    @Bean
    public KeyResolver apiKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getPath().value());
    }

    /**
     * 租户限流
     * @return 限流key
     */
    @Bean
    public KeyResolver tenantKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getHeaders().getFirst(EraSystemHeader.TENANT_ROUTE));
    }

}
