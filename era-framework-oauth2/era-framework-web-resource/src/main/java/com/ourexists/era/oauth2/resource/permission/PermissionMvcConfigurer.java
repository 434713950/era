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

package com.ourexists.era.oauth2.resource.permission;

import com.ourexists.era.framework.core.PathRule;
import com.ourexists.era.oauth2.core.interceptor.PermissionWhiteListProperties;
import com.ourexists.era.oauth2.core.interceptor.RegionHandlerInterceptor;
import com.ourexists.era.oauth2.core.store.PermissionStore;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author pengcheng
 * @date 2022/4/12 22:37
 * @since 1.0.0
 */
@Import(PermissionWhiteListProperties.class)
public class PermissionMvcConfigurer implements WebMvcConfigurer {

    private final PermissionWhiteListProperties whiteListProperties;

    private final PermissionStore permissionStore;

    private final Environment env;

    private final AntPathMatcher antPathMatcher;

    public PermissionMvcConfigurer(PermissionWhiteListProperties whiteListProperties,
                                   PermissionStore permissionStore,
                                   AntPathMatcher antPathMatcher,
                                   Environment env) {
        this.whiteListProperties = whiteListProperties;
        this.permissionStore = permissionStore;
        this.antPathMatcher = antPathMatcher;
        this.env = env;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RegionHandlerInterceptor(antPathMatcher, whiteListProperties))
                .addPathPatterns("/**")
                //swagger相关路径不走拦截器
                .excludePathPatterns(PathRule.HERDER_WHITE_PATHS)
                .order(1);
        registry.addInterceptor(new PermissionHandlerInterceptor(whiteListProperties, permissionStore, antPathMatcher, env))
                .addPathPatterns("/**")
                //swagger相关路径不走拦截器
                .excludePathPatterns(PathRule.SYSTEM_WHITE_PATH)
                .order(2);
    }
}
