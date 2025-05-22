/*
 * Copyright (c) 2025. created by ourexists.https://gitee.com/ourexists
 */

package com.ourexists.era.framework.webserver.gateway.filter.global.xss;

import com.ourexists.era.framework.webserver.gateway.config.GatewayError;
import com.ourexists.era.framework.webserver.gateway.filter.global.FilterOrder;
import com.ourexists.era.framework.webserver.gateway.filter.global.WebfluxRequestConverter;
import com.ourexists.era.framework.webserver.gateway.filter.global.WebfluxResponseConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Slf4j
@ConditionalOnProperty(
        prefix = "era.xss",
        name = "enabled",
        havingValue = "true"
)
public class XssRequestGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        HttpMethod method = request.getMethod();
        String contentType = request.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE);
        // GET请求处理
        if (HttpMethod.GET.equals(method)) {
            // 获取URI中的查询信息,并执行XSS扫描
            boolean pass = XssScanRuler.passGetRequestScan(request.getURI().getRawQuery());
            if (!pass) {
                return setUnauthorizedResponse(exchange);
            }
        }
        // POST和PUT请求处理
        if (postFlag(method, contentType) && typeCheck(exchange)) {
            ServerHttpRequestDecorator requestDecorator = (ServerHttpRequestDecorator) request;
            // 从请求里获取Post请求体,并执行XSS扫描
            boolean pass;
            // x-www-form-urlencoded类型走Get请求类型扫描
            if (MediaType.APPLICATION_FORM_URLENCODED_VALUE.equalsIgnoreCase(contentType)) {
                pass = XssScanRuler.passGetRequestScan(WebfluxRequestConverter.resolveBodyFromRequest(requestDecorator));
            } else {
                pass = XssScanRuler.passPostRequestScan(WebfluxRequestConverter.resolveBodyFromRequest(requestDecorator));
            }
            if (!pass) {
                return setUnauthorizedResponse(exchange);
            }
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return FilterOrder.XSS_REQUEST_FILTER_ORDER;
    }

    /**
     * 是否为满足过滤条件的POST请求
     * @param method
     * @param contentType
     * @return
     */
    private boolean postFlag(HttpMethod method, String contentType) {
        // POST、PUT请求中form和json类型的数据
        return (HttpMethod.POST.equals(method) || HttpMethod.PUT.equals(method))
                && (MediaType.APPLICATION_FORM_URLENCODED_VALUE.equalsIgnoreCase(contentType)
                || MediaType.APPLICATION_JSON_VALUE.equals(contentType)
                || MediaType.APPLICATION_JSON_UTF8_VALUE.equals(contentType));
    }

    /**
     * 设置403拦截状态
     */
    private Mono<Void> setUnauthorizedResponse(ServerWebExchange exchange) {
        return WebfluxResponseConverter.responseFailed(exchange, HttpStatus.FORBIDDEN.value(), GatewayError.UNSAFE_XSS_KEY);
    }

    /**
     * 类型检验
     * @param exchange
     * @return
     */
    private boolean typeCheck(ServerWebExchange exchange) {
        return exchange.getRequest() instanceof ServerHttpRequestDecorator;
    }
}
