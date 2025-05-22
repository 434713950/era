/*
 * Copyright (c) 2025. created by ourexists.https://gitee.com/ourexists
 */

package com.ourexists.era.framework.webserver.gateway.filter.global.encrypt;

import com.alibaba.fastjson2.JSONObject;
import com.ourexists.era.framework.core.utils.security.AESEncrypt;
import com.ourexists.era.framework.webserver.gateway.config.GatewayError;
import com.ourexists.era.framework.webserver.gateway.filter.global.EraGatewayGlobalFilter;
import com.ourexists.era.framework.webserver.gateway.filter.global.FilterOrder;
import com.ourexists.era.framework.webserver.gateway.filter.global.WebfluxResponseConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;


@Slf4j
@Configuration
@ConditionalOnBean(MaskingProperties.class)
public class RequestBodyDecryptGlobalFilter extends EraGatewayGlobalFilter {


    public RequestBodyDecryptGlobalFilter(AntPathMatcher antPathMatcher,
                                          MaskingProperties maskingProperties) {
        super(antPathMatcher, maskingProperties);
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 白名单
        if (isIgnore(exchange)) {
            return chain.filter(exchange);
        }
        String encryptData = "";
        // 判断是否为JSON数据类型的POST请求，是则继续
        if (!isJsonPost(exchange.getRequest())) {
            return chain.filter(exchange);
        }

        // 验证签名
        if (!verifyRequestSign(exchange.getRequest())) {
            log.error("[RequestBodyDecryptGlobalFilter]:验证请求签名失败，非法请求！sign={}", exchange.getRequest().getHeaders().getFirst(EncryptHeaders.SIGN_HEADER));
            return WebfluxResponseConverter.responseFailed(exchange, GatewayError.DATA_EXCEPTION);
        }

        // 取缓存的request body数据
        Object cacheRequestBodytObject = exchange.getAttribute(CacheRequestBodyGlobalFilter.CACHED_REQUEST_BODY_OBJECT_KEY);
        if (null == cacheRequestBodytObject) {
            log.error("[RequestBodyDecryptGlobalFilter]:从缓存中获取request body为空，Attributes={}", exchange.getAttributes());
            return WebfluxResponseConverter.responseFailed(exchange, GatewayError.DATA_EXCEPTION);
        }

        // 获取加密数据
        JSONObject jsonObject = JSONObject.parseObject((String) cacheRequestBodytObject);
        Object jo = jsonObject.get(maskingProperties.getSecretParamName());
        if (null != jo) {
            encryptData = (String) jo;
        }
        if (StringUtils.isEmpty(encryptData)) {
            log.error("[RequestBodyDecryptGlobalFilter]:从请求中获取的加密数据为空，请检查后重试！encryptData={}", encryptData);
            return WebfluxResponseConverter.responseFailed(exchange, GatewayError.DATA_EXCEPTION);
        }

        String decryptData;
        try {
            decryptData = AESEncrypt.AESDecode(maskingProperties.getSecretKey(), encryptData);
        } catch (Exception e) {
            log.error("[RequestBodyDecryptGlobalFilter]:解密请求数据异常，请联系系统管理员处理！待解密数据：{}", encryptData);
            return WebfluxResponseConverter.responseFailed(exchange, GatewayError.DATA_EXCEPTION);
        }

        // 构建新的请求
        ServerHttpRequest newRequest = getServerHttpRequest(exchange, decryptData);
        // 把解密后的数据重置到exchange自定义属性中, 在之后的拦截器中可获取解密后的请求体数据
        exchange.getAttributes().put(CacheRequestBodyGlobalFilter.CACHED_REQUEST_BODY_OBJECT_KEY, decryptData);
        return chain.filter(exchange.mutate().request(newRequest).build());
    }

    /**
     * 验证签名
     * @param request
     * @return
     */
    private boolean verifyRequestSign(ServerHttpRequest request) {
        String token = request.getHeaders().getFirst(EncryptHeaders.TOKEN_HEADER);
        String timestamp = request.getHeaders().getFirst(EncryptHeaders.TIMESTAMP_HEADER);
        String sign = request.getHeaders().getFirst(EncryptHeaders.SIGN_HEADER);
        if (sign == null) {
            return false;
        }
        return sign.equals(DigestUtils.sha1Hex(timestamp + token + maskingProperties.getSignKey()));
    }

    /**
     * 构建新的请求
     * @param exchange
     * @param decryptData
     * @return
     */
    private ServerHttpRequest getServerHttpRequest(ServerWebExchange exchange, String decryptData) {
        byte[] decrypBytes = decryptData.getBytes(StandardCharsets.UTF_8);
        // 根据解密后的参数重新构建请求
        DataBufferFactory dataBufferFactory = exchange.getResponse().bufferFactory();
        Flux<DataBuffer> bodyFlux = Flux.just(dataBufferFactory.wrap(decrypBytes));
        ServerHttpRequest newRequest = exchange.getRequest().mutate().uri(exchange.getRequest().getURI()).build();
        newRequest = new ServerHttpRequestDecorator(newRequest) {
            @Override
            public HttpHeaders getHeaders() {
                // 构建新的请求头
                HttpHeaders headers = new HttpHeaders();
                headers.putAll(exchange.getRequest().getHeaders());
                // 由于修改了传递参数，需要重新设置CONTENT_LENGTH，长度是字节长度，不是字符串长度
                int length = decrypBytes.length;
                headers.remove(HttpHeaders.CONTENT_LENGTH);
                headers.setContentLength(length);
                headers.set(HttpHeaders.CONTENT_TYPE, "application/json");
                return headers;
            }

            @Override
            public Flux<DataBuffer> getBody() {
                return bodyFlux;
            }
        };
        return newRequest;
    }

    @Override
    public int getOrder() {
        return FilterOrder.DECRYPT_REQUEST_FILTER_ORDER;
    }

    /**
     * 判断是否为JSON类型的POST请求
     * @param request
     * @return
     */
    private boolean isJsonPost(ServerHttpRequest request) {
        String contentType = request.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE);
        return HttpMethod.POST.equals(request.getMethod())
                && (MediaType.APPLICATION_JSON_VALUE.equals(contentType)
                || MediaType.APPLICATION_JSON_UTF8_VALUE.equals(contentType));

    }



}
