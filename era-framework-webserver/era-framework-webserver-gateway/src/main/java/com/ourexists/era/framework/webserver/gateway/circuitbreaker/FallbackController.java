package com.ourexists.era.framework.webserver.gateway.circuitbreaker;

import com.ourexists.era.framework.core.model.vo.JsonResponseEntity;
import com.ourexists.era.framework.webserver.gateway.config.GatewayError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

    /**
     * 熔断降级
     * @return
     */
    @RequestMapping(value = "/circuitBreakerFallback", method = RequestMethod.GET)
    public JsonResponseEntity<?> circuitBreakerFallback() {
        return new JsonResponseEntity<>(GatewayError.SYSTEM_BUSY.getCode(), GatewayError.SYSTEM_BUSY.getMessage());
    }


    /**
     * 限流降级
     * @return
     */
    @RequestMapping(value = "/rateLimitFallback", method = RequestMethod.GET)
    public JsonResponseEntity<?> rateLimitFallback() {
        return new JsonResponseEntity<>(GatewayError.RATE_LIMIT.getCode(), GatewayError.RATE_LIMIT.getMessage());
    }
}
