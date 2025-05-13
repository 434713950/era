/*
 * Copyright © 2022 www.wondersgroup.com. All rights reserved.
 */
package com.ourexists.era.oauth2.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author pengcheng
 * @date 2022/4/8 14:03
 * @since 1.0.0
 */
public class PathRule {

    /**
     * 统一认证api的前缀
     */
    public static final String AUTHENTICATION_API_PREFIX = "/authentication";

    /**
     * 统一开放接口前缀
     */
    public static final String OPEN_PREFIX = "/open";

    /**
     * 用户认证白名单前缀
     */
    public static final String WHITE_PREFIX = "/white";

    /**
     * 全量数据接口前缀
     */
    public static final String OVERALL_PREFIX = "/overall";

    /**
     * 限定数据接口前缀。即主租户的数据将会限制自身
     */
    public static final String LIMIT_PREFIX = "/limit";

    public static final String[] OPEN_API_PATHS = {"/swagger-ui.html", "/v2/**", "/swagger-resources/**", "/swagger-ui.html/**", "/swagger-ui/**"};

    public static final String[] HEALTH_PATHS = {"/health", "/info", "/refresh"};

    public static final String[] STATIC_PATHS = {"/static/**", "/error", "/webjars/**"};

    public static final String[] OAUTH_PATHS = {"/oauth/**"};

    /**
     * 请求头校验白名单路径
     */
    public static final List<String> HERDER_WHITE_PATHS;

    /**
     * 用户认证白名单路径
     */
    public static final List<String> SYSTEM_WHITE_PATH;

    static {
        HERDER_WHITE_PATHS = new ArrayList<>();
        HERDER_WHITE_PATHS.addAll(Arrays.asList(HEALTH_PATHS));
        HERDER_WHITE_PATHS.addAll(Arrays.asList(OPEN_API_PATHS));
        HERDER_WHITE_PATHS.addAll(Arrays.asList(STATIC_PATHS));
        HERDER_WHITE_PATHS.addAll(Arrays.asList(OAUTH_PATHS));
        HERDER_WHITE_PATHS.add(OPEN_PREFIX + "/**");

        SYSTEM_WHITE_PATH = new ArrayList<>();
        SYSTEM_WHITE_PATH.addAll(HERDER_WHITE_PATHS);
        SYSTEM_WHITE_PATH.addAll(Arrays.asList(AUTHENTICATION_API_PREFIX + "/**", WHITE_PREFIX + "/**"));
    }
}
