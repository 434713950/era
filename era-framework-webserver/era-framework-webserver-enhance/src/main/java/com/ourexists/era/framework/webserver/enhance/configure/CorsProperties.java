/*
 * Copyright (c) 2025. created by ourexists.https://gitee.com/ourexists
 */

package com.ourexists.era.framework.webserver.enhance.configure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author pengcheng
 * @date 2022/4/22 14:42
 * @since 1.0.0
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "era.security.cors")
public class CorsProperties {

    private List<String> allowedOrigins = Collections.singletonList("*");


    private List<String> allowedMethods = Arrays.asList("GET", "POST", "DELETE", "PUT", "OPTIONS");

    private List<String> allowedHeaders = Collections.singletonList("*");

    private Boolean allowCredentials = true;
}
