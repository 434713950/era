package com.ourexists.era.framework.webserver.gateway.config;

import com.alibaba.fastjson2.JSONObject;
import com.ourexists.era.framework.core.constants.ResultMsgEnum;
import com.ourexists.era.framework.core.model.vo.JsonResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Slf4j
public class DefaultGatewayAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        log.error(ex.getMessage(), ex);
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        JsonResponseEntity<?> jo = new JsonResponseEntity<>(ResultMsgEnum.SC_UNAUTHORIZED.getResultCode(), ex.getMessage());
        String json = JSONObject.toJSONString(jo);
        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                .bufferFactory()
                .wrap(json.getBytes(StandardCharsets.UTF_8))));
    }
}
