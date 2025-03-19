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

package com.ourexists.era.framework.oauth2;

import com.ourexists.era.framework.oauth2.handler.EmptyEraAccessDeniedHandler;
import com.ourexists.era.framework.oauth2.handler.EmptyEraAuthenticationEntryPoint;
import com.ourexists.era.framework.oauth2.handler.EraAccessDeniedHandler;
import com.ourexists.era.framework.oauth2.handler.EraAuthenticationEntryPoint;
import com.ourexists.era.framework.oauth2.resource.permission.store.InMemoryPermissionStore;
import com.ourexists.era.framework.oauth2.resource.permission.store.PermissionStore;
import com.ourexists.era.framework.oauth2.security.EraPasswordEncoder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author pengcheng
 * @date 2022/4/18 9:34
 * @since 1.0.0
 */
public class PublicServerConfigurer {

    @Bean
    @ConditionalOnMissingBean(PermissionStore.class)
    public PermissionStore permissionStore() {
        return new InMemoryPermissionStore();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new EraPasswordEncoder();
    }

    @Bean
    @ConditionalOnMissingBean(EraAuthenticationEntryPoint.class)
    public EraAuthenticationEntryPoint eraAuthenticationEntryPoint() {
        return new EmptyEraAuthenticationEntryPoint();
    }

    @Bean
    @ConditionalOnMissingBean(EraAccessDeniedHandler.class)
    public EraAccessDeniedHandler eraAccessDeniedHandler() {
        return new EmptyEraAccessDeniedHandler();
    }
}
