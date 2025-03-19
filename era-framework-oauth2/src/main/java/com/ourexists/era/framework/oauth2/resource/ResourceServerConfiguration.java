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

package com.ourexists.era.framework.oauth2.resource;

import com.ourexists.era.framework.oauth2.AuthConstants;
import com.ourexists.era.framework.oauth2.UserManager;
import com.ourexists.era.framework.oauth2.handler.EraAccessDeniedHandler;
import com.ourexists.era.framework.oauth2.handler.EraAuthenticationEntryPoint;
import com.ourexists.era.framework.oauth2.resource.permission.PermissionMvcConfigurer;
import com.ourexists.era.framework.oauth2.resource.permission.PermissionWhiteListProperties;
import com.ourexists.era.framework.oauth2.resource.permission.WhiteFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pengcheng
 * @date 2022/4/13 23:23
 * @since 1.0.0
 */
@EnableResourceServer
@Import({PermissionMvcConfigurer.class, WhiteFilter.class})
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Value("${spring.application.name}")
    private String resourceId;

    @Autowired
    private EraAuthenticationEntryPoint point;

    @Autowired
    private EraAccessDeniedHandler accessDeniedHandler;

    @Autowired
    private PermissionWhiteListProperties permissionWhiteListProperties;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        List<String> whites = new ArrayList<>();
        whites.addAll(permissionWhiteListProperties.getAuthCheck());
        whites.addAll(AuthConstants.SYSTEM_WHITE_PATH);
        http
                .cors()
                .disable()
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(point)
                .accessDeniedHandler(accessDeniedHandler)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(whites.toArray(new String[whites.size()]))
                .permitAll()
                .anyRequest()
                .authenticated();
        // 禁用缓存
        http.headers().cacheControl();
        http.headers().frameOptions().sameOrigin();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        if (!StringUtils.isEmpty(resourceId)) {
            resources.resourceId(resourceId);
        }
        super.configure(resources);
    }

    @Bean
    public UserManager userManager(TokenStore tokenStore) {
        return new UserManager(tokenStore);
    }

}
