/*
 * Copyright (c) 2025. created by ourexists.https://gitee.com/ourexists
 */

package com.ourexists.era.framework.webserver.enhance.configure;

import com.ourexists.era.framework.core.PathRule;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author pengcheng
 * @date 2021/9/1 10:58
 * @since 1.0.0
 */
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final ApplicationContext applicationContext;

    public WebMvcConfiguration(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoggableHandlerInterceptor(applicationContext))
                .addPathPatterns("/**")
                .excludePathPatterns(PathRule.SYSTEM_WHITE_PATH);
    }
}
