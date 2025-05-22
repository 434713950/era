/*
 * Copyright (c) 2025. created by ourexists.https://gitee.com/ourexists
 */

package com.ourexists.era.framework.webserver.enhance.configure;

import com.ourexists.era.framework.webserver.enhance.I18nUtil;
import com.ourexists.era.framework.webserver.enhance.handler.ExceptionAnalysisHandler;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

/**
 * @author pengcheng
 * @date 2022/2/21 15:51
 * @since 1.0.0
 */
@Configuration
@Import({OpenApiConfigurer.class,
        LocaleConfigurer.class,
        WebMvcConfiguration.class,
        EraSecurityConfigurer.class,
        ExceptionAnalysisHandler.class})
public class EraEnhanceConfiguration {

    @Bean
    public I18nUtil i18nUtil(MessageSource messageSource) {
        return new I18nUtil(messageSource);
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }

    @Bean
    public AntPathMatcher antPathMatcher() {
        return new AntPathMatcher();
    }
}
