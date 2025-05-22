/*
 * Copyright (c) 2025. created by ourexists.https://gitee.com/ourexists
 */

package com.ourexists.era.framework.webserver.enhance.configure;

import com.ourexists.era.framework.core.EraSystemHeader;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.method.HandlerMethod;

/**
 * @Author: PengCheng
 * @Description: swagger2配置
 * @Date: 23:27 2018/4/19/019
 */
@Import(OpenApiProperties.class)
public class OpenApiConfigurer {

    @Bean
    public OpenAPI openAPI(OpenApiProperties openApiProperties) {
        Contact contact = new Contact();
        contact.setName(openApiProperties.getContactName());
        contact.setUrl(openApiProperties.getContactUrl());
        contact.setEmail(openApiProperties.getContactEmail());
        return new OpenAPI()
                .info(new Info()
                        .title(openApiProperties.getTitle())
                        .description(openApiProperties.getDescription())
                        .contact(contact)
                        .version(openApiProperties.getVersion())
                );
    }

    @Bean
    public OperationCustomizer customGlobalHeaders() {

        return (Operation operation, HandlerMethod handlerMethod) -> {
            operation.addParametersItem(new Parameter()
                            .in(ParameterIn.HEADER.toString())
                            .schema(new StringSchema())
                            .name(EraSystemHeader.PLATFORM_HEADER)
                            .description("归属平台")
                            .required(true))
                    .addParametersItem(new Parameter()
                            .in(ParameterIn.HEADER.toString())
                            .schema(new StringSchema())
                            .name(EraSystemHeader.TENANT_ROUTE)
                            .description("归属租户"))
                    .addParametersItem(new Parameter()
                            .in(ParameterIn.HEADER.toString())
                            .schema(new StringSchema())
                            .name(EraSystemHeader.AUTH_HEADER)
                            .description("认证头"))
                    .addParametersItem(new Parameter()
                            .in(ParameterIn.HEADER.toString())
                            .schema(new StringSchema())
                            .name(EraSystemHeader.REAL_TENANT_ROUTE)
                            .description("真实租户"));
            return operation;
        };
    }
}
