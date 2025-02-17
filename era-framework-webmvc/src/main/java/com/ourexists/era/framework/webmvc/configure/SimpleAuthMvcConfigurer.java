/*
 * Copyright (C) 2025  ChengPeng
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package com.ourexists.era.framework.webmvc.configure;

import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

/**
 * @author pengcheng
 * @date 2022/4/12 22:37
 * @since 1.0.0
 */
public class SimpleAuthMvcConfigurer implements WebMvcConfigurer {

    private final SimpleAuthWhiteListProperties simpleAuthWhiteListProperties;

    private final AntPathMatcher antPathMatcher;

    public SimpleAuthMvcConfigurer(SimpleAuthWhiteListProperties simpleAuthWhiteListProperties,
                                   AntPathMatcher antPathMatcher) {
        this.simpleAuthWhiteListProperties = simpleAuthWhiteListProperties;
        this.antPathMatcher = antPathMatcher;
    }

    /**
     * 统一认证api的前缀
     */
    public static String AUTHENTICATION_API_PREFIX = "/authentication";

    public static String OPEN_PREFIX = "/open";


    public static String WHITE_PREFIX = "/white";

    /**
     * 全局数据接口前缀
     */
    public static String OVERALL_PREFIX = "/overall";

    public static String OVERALL_PATH = OVERALL_PREFIX + "/**";

    /**
     * 限定数据接口前缀。即主租户的数据将会限制自身
     */
    public static String LIMIT_PREFIX = "/limit";

    public static String LIMIT_PATH = LIMIT_PREFIX + "/**";

    /**
     * 许可信息
     */
    public static String LICENSE = "made by wondersgroup";

    /**
     * 系统相关的白名单path
     */
    public static List<String> SYSTEM_WHITE_PATH = Arrays.asList("/swagger-resources/**", "/swagger-ui/**",
            "/webjars/**",  "/v3/**", "/oauth/**", "/refresh", AUTHENTICATION_API_PREFIX + "/**",
            OPEN_PREFIX + "/**", WHITE_PREFIX + "/**", "/error", "/health");

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RegionHandlerInterceptor())
                .addPathPatterns("/**")
                .order(10);
        registry.addInterceptor(new SimpleAuthHandlerInterceptor(simpleAuthWhiteListProperties, antPathMatcher))
                .addPathPatterns("/**")
                //swagger相关路径不走拦截器
                .excludePathPatterns(SYSTEM_WHITE_PATH)
                .order(11);
    }
}
