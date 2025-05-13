/*
 * Copyright © 2022 www.wondersgroup.com. All rights reserved.
 */
package com.ourexists.era.oauth2.core;

import java.util.Arrays;
import java.util.List;

/**
 * @author pengcheng
 * @date 2022/4/8 14:03
 * @since 1.0.0
 */
public interface AuthConstants {

    /**
     * 统一认证api的前缀
     */
    String AUTHENTICATION_API_PREFIX = "/authentication";

    String OPEN_PREFIX = "/open";


    String WHITE_PREFIX = "/white";

    /**
     * 全局数据接口前缀
     */
    String OVERALL_PREFIX = "/overall";

    String OVERALL_PATH  = OVERALL_PREFIX+"/**";

    /**
     * 限定数据接口前缀。即主租户的数据将会限制自身
     */
    String LIMIT_PREFIX = "/limit";

    String LIMIT_PATH  = LIMIT_PREFIX+"/**";

    /**
     * 许可信息
     */
    String LICENSE = "made by ourexists";

    List<String> HEADER_WHITE_PATH = Arrays.asList("/swagger-resources/**", "/swagger-ui.html/**",
            OPEN_PREFIX + "/**",
            "/webjars/**", "/swagger-ui.html", "/v2/**", "/oauth/**", "/refresh", "/error", "/health");

    /**
     * 系统相关的白名单path
     */
    List<String> SYSTEM_WHITE_PATH = Arrays.asList("/swagger-resources/**", "/swagger-ui.html/**",
            "/webjars/**", "/swagger-ui.html", "/v2/**", "/oauth/**", "/refresh", AUTHENTICATION_API_PREFIX + "/**",
            OPEN_PREFIX + "/**", WHITE_PREFIX + "/**", "/error", "/health");


}
