/*
 * Copyright (c) 2025. created by ourexists.https://gitee.com/ourexists
 */

package com.ourexists.era.framework.webserver.enhance.configure;

import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p></p>
 *
 * @author PengCheng
 * @date 2018/12/12
 */
@Data
@ConfigurationProperties(prefix = "era.openapi")
public class OpenApiProperties {

    private String title;

    private String description;

    @URL
    private String serviceUrl;

    private String version = "1.0.0";

    private String contactName = "pengcheng";

    @URL
    private String contactUrl;

    @Email
    private String contactEmail;

}
