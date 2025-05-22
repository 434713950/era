/*
 * Copyright (c) 2025. created by ourexists.https://gitee.com/ourexists
 */

package com.ourexists.era.framework.webserver.gateway.filter.global.encrypt;

import com.ourexists.era.framework.webserver.gateway.filter.global.FilterOrder;
import io.netty.buffer.UnpooledByteBufAllocator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class CacheRequestBodyGlobalFilter implements GlobalFilter, Ordered {

    public static final String CACHED_REQUEST_BODY_OBJECT_KEY = "cached_request_body_object_key";

    private ServerCodecConfigurer codecConfigurer;

    public CacheRequestBodyGlobalFilter(ServerCodecConfigurer codecConfigurer) {
        this.codecConfigurer = codecConfigurer;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpMethod method = exchange.getRequest().getMethod();
        String contentType = exchange.getRequest().getHeaders().getFirst(HttpHeaders.CONTENT_TYPE);
        // 只拦截POST、PUT请求中x-www-form-urlencoded和json类型的数据
        if (postFlag(method, contentType)) {
            // 如果body已经缓存过，略过
            if (isCached(exchange)) {
                return chain.filter(exchange);
            }
            // 构建新的serverRequest
            ServerRequest serverRequest = ServerRequest.create(exchange, codecConfigurer.getReaders());
            // 将请求中的body转String类型
            Mono<String> bodyToMono = serverRequest.bodyToMono(String.class).defaultIfEmpty("");
            try {
                return bodyToMono.flatMap(body -> {
                    // 将request body中的内容copy一份，记录到exchange的一个自定义属性中
                    exchange.getAttributes().put(CACHED_REQUEST_BODY_OBJECT_KEY, body);
                    ServerHttpRequestDecorator newRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
                        @Override
                        public HttpHeaders getHeaders() {
                            HttpHeaders httpHeaders = new HttpHeaders();
                            httpHeaders.putAll(super.getHeaders());
                            // 设置报文传输编码为分块编码（chunked）
                            httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
                            return httpHeaders;
                        }

                        @Override
                        public Flux<DataBuffer> getBody() {
                            NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(new UnpooledByteBufAllocator(false));
                            DataBuffer bodyDataBuffer = nettyDataBufferFactory.wrap(body.getBytes());
                            return Flux.just(bodyDataBuffer);
                        }
                    };
                    return chain.filter(exchange.mutate().request(newRequest).build());
                });
            } catch (Exception e) {
                log.error("[CacheRequestBodyGlobalFilter]: 缓存请求body异常，message={}", e.getMessage());
            }
        }
        return chain.filter(exchange);
    }

    /**
     * 是否为满足过滤条件的POST请求
     * @param method
     * @param contentType
     * @return
     */
    private boolean postFlag(HttpMethod method, String contentType) {
        // POST、PUT请求中x-www-form-urlencoded和json类型的数据
        return (HttpMethod.POST.equals(method) || HttpMethod.PUT.equals(method))
                && (MediaType.APPLICATION_FORM_URLENCODED_VALUE.equalsIgnoreCase(contentType)
                || MediaType.APPLICATION_JSON_VALUE.equals(contentType)
                || MediaType.APPLICATION_JSON_UTF8_VALUE.equals(contentType));
    }

    /**
     * body是否缓存过
     * @param exchange
     * @return
     */
    private boolean isCached(ServerWebExchange exchange) {
        return null != exchange.getAttributeOrDefault(CACHED_REQUEST_BODY_OBJECT_KEY, null);
    }

    @Override
    public int getOrder() {
        return FilterOrder.CACHE_REQUEST_FILTER_ORDER;
    }
}
