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

import com.ourexists.era.framework.core.utils.AuthUtils;
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
 * @Description:    swagger2配置
 * @Date: 23:27 2018/4/19/019
 */
@Import(OpenApiProperties.class)
public class OpenApiConfigurer {

    @Autowired
    public OpenApiProperties openApiProperties;

    @Bean
    public OpenAPI openAPI() {
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
                            .name(AuthUtils.PLATFORM_HEADER)
                            .description("归属平台")
                            .required(true))
                    .addParametersItem(new Parameter()
                            .in(ParameterIn.HEADER.toString())
                            .schema(new StringSchema())
                            .name(AuthUtils.TENANT_ROUTE)
                            .description("归属租户"))
                    .addParametersItem(new Parameter()
                            .in(ParameterIn.HEADER.toString())
                            .schema(new StringSchema())
                            .name(AuthUtils.AUTH_HEADER)
                            .description("认证头"));
            return operation;
        };
    }
}
