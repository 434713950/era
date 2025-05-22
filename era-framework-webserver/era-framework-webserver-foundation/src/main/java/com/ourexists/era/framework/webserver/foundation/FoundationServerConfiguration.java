/*
 * Copyright (c) 2025. created by ourexists.https://gitee.com/ourexists
 */

package com.ourexists.era.framework.webserver.foundation;

import com.ourexists.era.framework.core.PathRule;
import com.ourexists.era.oauth2.core.interceptor.HeaderHandlerInterceptor;
import com.ourexists.era.oauth2.core.interceptor.PermissionWhiteListProperties;
import com.ourexists.era.oauth2.core.interceptor.RegionHandlerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author pengcheng
 * @date 2022/4/12 22:37
 * @since 1.0.0
 */
@Configuration
@Import({PermissionWhiteListProperties.class})
public class FoundationServerConfiguration implements WebMvcConfigurer {

    private final PermissionWhiteListProperties permissionWhiteListProperties;

    private final AntPathMatcher antPathMatcher;

    public FoundationServerConfiguration(PermissionWhiteListProperties permissionWhiteListProperties) {
        this.permissionWhiteListProperties = permissionWhiteListProperties;
        this.antPathMatcher = new AntPathMatcher();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HeaderHandlerInterceptor()).addPathPatterns("/**").order(1);
        registry.addInterceptor(new RegionHandlerInterceptor(antPathMatcher, permissionWhiteListProperties)).addPathPatterns("/**")
                //swagger相关路径不走拦截器
                .excludePathPatterns(PathRule.SYSTEM_WHITE_PATH).order(2);
    }
}
