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
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

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

        Components components = new Components()
                .addSecuritySchemes(AuthUtils.AUTH_HEADER, new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT"))
                .addSchemas(AuthUtils.PLATFORM_HEADER, new Schema<>().type("string"))
                .addSchemas(AuthUtils.TENANT_ROUTE, new Schema<>().type("string"))
                .addSchemas(SimpleAuthUtils.REAL_TENANT_ROUTE, new Schema<>().type("string"));

        return new OpenAPI()
                .info(new Info()
                        .title(openApiProperties.getTitle())
                        .description(openApiProperties.getDescription())
                        .contact(contact)
                        .version(openApiProperties.getVersion())
                )
                .components(components);
    }
}
