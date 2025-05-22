/*
 * Copyright (c) 2025. created by ourexists.https://gitee.com/ourexists
 */

package com.ourexists.era.framework.webserver.gateway.filter.global;

import com.alibaba.fastjson2.JSONObject;
import com.ourexists.era.framework.core.model.vo.JsonResponseEntity;
import com.ourexists.era.framework.webserver.gateway.config.GatewayError;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

public class WebfluxResponseConverter {

    /**
     * 封装异常信息时的response
     * @param exchange
     * @param errorType 异常类型
     * @return
     */
    public static Mono<Void> responseFailed(ServerWebExchange exchange, GatewayError errorType) {
        return responseFailed(exchange, HttpStatus.INTERNAL_SERVER_ERROR.value(), errorType);
    }

    /**
     * 封装异常信息时的response
     * @param exchange
     * @param httpStatus http的响应状态
     * @param errorType 异常类型
     * @return
     */
    public static Mono<Void> responseFailed(ServerWebExchange exchange, int httpStatus, GatewayError errorType) {
        JsonResponseEntity<?> entity = new JsonResponseEntity<>(errorType.getCode(), errorType.getMessage());
        return responseWrite(exchange, httpStatus, entity);
    }

    /**
     * 构建response
     * @param exchange
     * @param httpStatus HTTP状态
     * @param result
     * @return
     */
    public static Mono<Void> responseWrite(ServerWebExchange exchange, int httpStatus, JsonResponseEntity<?> result) {
        DataBuffer buffer = getDataBuffer(exchange, httpStatus, JSONObject.toJSONString(result));
        return exchange.getResponse().writeWith(Mono.just(buffer)).doOnError((error) -> DataBufferUtils.release(buffer));
    }

    /**
     * 构建DataBuffer
     * @param exchange
     * @param httpStatus HTTP状态
     * @param responseBody 响应体
     * @return
     */
    private static DataBuffer getDataBuffer(ServerWebExchange exchange, int httpStatus, String responseBody) {
        if (httpStatus == 0) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR.value();
        }
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.valueOf(httpStatus));
        return response.bufferFactory().wrap(responseBody.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 将对象转成DataBuffer
     * @param exchange
     * @param errorType 异常类型
     * @param httpStatus HTTP状态
     * @return
     */
    public static DataBuffer transfromObjectToDataBuffer(ServerWebExchange exchange, int httpStatus, GatewayError errorType) {
        JsonResponseEntity<?> entity = new JsonResponseEntity<>(errorType.getCode(), errorType.getMessage());
        return getDataBuffer(exchange, httpStatus, JSONObject.toJSONString(entity));
    }

}
