package com.ourexists.era.framework.webserver.gateway.config;

import lombok.Getter;

/**
 * @Author: zhengcan
 * @Date: 2022/3/21
 * @Description: 系统异常描述
 * @Version: 1.0.0 创建
 */
@Getter
public enum GatewayError {

    SYSTEM_BUSY(502, "服务当前不可用,请稍后再试!"),
    RATE_LIMIT(429, "当前请求繁忙,请稍后再试!"),
    UNSAFE_XSS_KEY(400, "存在有安全隐患的非法请求!"),
    DATA_EXCEPTION(400, "存在无法解读的非法请求!");

    /**
     * 错误类型码
     */
    private final Integer code;
    /**
     * 错误类型描述信息
     */
    private final String message;

    GatewayError(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
