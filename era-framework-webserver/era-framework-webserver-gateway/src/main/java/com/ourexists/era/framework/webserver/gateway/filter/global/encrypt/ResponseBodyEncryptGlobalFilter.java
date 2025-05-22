/*
 * Copyright (c) 2025. created by ourexists.https://gitee.com/ourexists
 */

package com.ourexists.era.framework.webserver.gateway.filter.global.encrypt;

import com.alibaba.fastjson2.JSONObject;
import com.ourexists.era.framework.core.utils.CollectionUtil;
import com.ourexists.era.framework.core.utils.security.AESEncrypt;
import com.ourexists.era.framework.webserver.gateway.config.GatewayError;
import com.ourexists.era.framework.webserver.gateway.filter.global.EraGatewayGlobalFilter;
import com.ourexists.era.framework.webserver.gateway.filter.global.FilterOrder;
import com.ourexists.era.framework.webserver.gateway.filter.global.WebfluxResponseConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.reactivestreams.Publisher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;


@Slf4j
@Configuration
@ConditionalOnBean(MaskingProperties.class)
public class ResponseBodyEncryptGlobalFilter extends EraGatewayGlobalFilter {

    public ResponseBodyEncryptGlobalFilter(AntPathMatcher antPathMatcher,
                                           MaskingProperties maskingProperties) {
        super(antPathMatcher, maskingProperties);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 白名单
        if (isIgnore(exchange)) {
            return chain.filter(exchange);
        }
        ServerHttpResponse response = exchange.getResponse();
        ServerHttpResponseDecorator responseDecorator = new ServerHttpResponseDecorator(response) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (HttpStatus.OK.equals(getStatusCode()) && body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                    // 加密响应数据并重写DataBuffer
                    return super.writeWith(fluxBody.buffer().map(dataBuffers ->
                            encryptAndRewriteDataBuffer(dataBuffers, exchange, response)));
                }
                return super.writeWith(body);
            }
        };

        return chain.filter(exchange.mutate().response(responseDecorator).build());
    }

    /**
     * 加密响应数据并重写DataBuffer
     *
     * @param dataBuffers
     * @param exchange
     * @param response
     * @return
     */
    private DataBuffer encryptAndRewriteDataBuffer(List<? extends DataBuffer> dataBuffers, ServerWebExchange exchange, ServerHttpResponse response) {
        DataBufferFactory dataBufferFactory = response.bufferFactory();
        DataBuffer join = dataBufferFactory.join(dataBuffers);
        byte[] content = new byte[join.readableByteCount()];
        join.read(content);
        // 释放掉内存
        DataBufferUtils.release(join);
        // 未加密的返回数据
        String contentStr = new String(content, StandardCharsets.UTF_8);
        // 返回响应值非200状态时不加密
        if (!checkIsOK(contentStr)) {
            return dataBufferFactory.wrap(contentStr.getBytes(StandardCharsets.UTF_8));
        }
        // 使用AES进行加密
        JSONObject jsonObject = new JSONObject();
        try {
            String encryptData = AESEncrypt.AESEncode(maskingProperties.getSecretKey(), contentStr);
            jsonObject.put(maskingProperties.getSecretParamName(), encryptData);
        } catch (Exception e) {
            log.error("[ResponseBodyEncryptGlobalFilter]:加密响应数据异常，请联系系统管理员处理！待加密数据：{}", contentStr);
            return WebfluxResponseConverter.transfromObjectToDataBuffer(exchange, HttpStatus.INTERNAL_SERVER_ERROR.value(), GatewayError.DATA_EXCEPTION);
        }
        String responseStr = jsonObject.toJSONString();
        byte[] responseBytes = new String(responseStr.getBytes(), StandardCharsets.UTF_8).getBytes();
        setReaponseHeaders(exchange, response, responseBytes);
        return dataBufferFactory.wrap(responseBytes);
    }

    /**
     * 设置响应头
     *
     * @param exchange
     * @param response
     * @param responseBytes
     */
    private void setReaponseHeaders(ServerWebExchange exchange, ServerHttpResponse response, byte[] responseBytes) {
        // 重置响应体长度
        response.getHeaders().remove(HttpHeaders.CONTENT_LENGTH);
        response.getHeaders().setContentLength(responseBytes.length);
        // 获取时间戳
        String timestamp = Long.toString(System.currentTimeMillis());
        // 获取token
        String token = exchange.getRequest().getHeaders().getFirst(EncryptHeaders.TOKEN_HEADER);
        response.getHeaders().set(EncryptHeaders.TIMESTAMP_HEADER, timestamp);
        response.getHeaders().set(EncryptHeaders.TOKEN_HEADER, (null == token) ? "" : token);
        response.getHeaders().set(EncryptHeaders.SIGN_HEADER, DigestUtils.sha1Hex(timestamp + token + maskingProperties.getSignKey()));
    }

    /**
     * 检验响应的返回值是否成功
     *
     * @param responseBody
     * @return
     */
    private boolean checkIsOK(String responseBody) {
        JSONObject jsonObject = JSONObject.parseObject(responseBody);
        Object obj = jsonObject.get("code");
        int code;
        if (null != obj) {
            code = (int) obj;
        } else {
            return true;
        }
        return HttpStatus.OK.value() == code;
    }

    @Override
    public int getOrder() {
        return FilterOrder.ENCRYPT_RESPONSE_FILTER_ORDER;
    }
}
