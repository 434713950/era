/*
 * Copyright (c) 2025. created by ourexists.https://gitee.com/ourexists
 */

package com.ourexists.era.framework.webserver.gateway.filter.global.encrypt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@ConditionalOnProperty(
        prefix = "era.masking",
        name = "enabled",
        havingValue = "true"
)
@ConfigurationProperties(prefix = "era.masking")
public class MaskingProperties {

    /**
     * 忽略加密urls
     */
    private List<String> ignoreUrls = new ArrayList<>();

    /**
     * 加密key
     */
    private String secretKey = "bHeGI91r";

    /**
     * 签名加密key
     */
    private String signKey = "QGZUanoSaSy9DEVQFVULJQ==";

    /**
     * 加密的参数名
     */
    private String secretParamName = "data";
}
