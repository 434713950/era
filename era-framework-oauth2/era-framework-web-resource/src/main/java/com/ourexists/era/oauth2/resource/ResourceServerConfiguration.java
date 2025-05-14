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

package com.ourexists.era.oauth2.resource;

import com.ourexists.era.oauth2.core.PathRule;
import com.ourexists.era.oauth2.core.handler.EmptyEraAccessDeniedHandler;
import com.ourexists.era.oauth2.core.handler.EmptyEraAuthenticationEntryPoint;
import com.ourexists.era.oauth2.core.handler.EraAccessDeniedHandler;
import com.ourexists.era.oauth2.core.handler.EraAuthenticationEntryPoint;
import com.ourexists.era.oauth2.resource.permission.PermissionMvcConfigurer;
import com.ourexists.era.oauth2.resource.permission.PermissionWhiteListProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pengcheng
 * @date 2022/4/13 23:23
 * @since 1.0.0
 */
@EnableWebSecurity
@Import({PermissionMvcConfigurer.class,
//        AuthHeaderWhiteHandleFilter.class,
        PermissionWhiteListProperties.class})
public class ResourceServerConfiguration {

//    @Value("${spring.application.name}")
//    private String resourceId;

    @Bean
    public SecurityFilterChain securityFilterChain(PermissionWhiteListProperties permissionWhiteListProperties,
                                                   HttpSecurity http) throws Exception {
        List<String> whites = new ArrayList<>();
        whites.addAll(permissionWhiteListProperties.getAuthCheck());
        whites.addAll(PathRule.SYSTEM_WHITE_PATH);
        http
//                .cors(Customizer.withDefaults())
//                .csrf(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(whites.toArray(new String[whites.size()])).permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(Customizer.withDefaults()) // 启用 JWT 解码
                );
        http.headers(headers -> headers
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                .cacheControl(Customizer.withDefaults())
        );
        return http.build();
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


//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//
//        http
//                .cors()
//                .disable()
//                .csrf()
//                .disable()
//                .exceptionHandling()
//                .authenticationEntryPoint(point)
//                .accessDeniedHandler(accessDeniedHandler)
//                .and()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeRequests()
//                .antMatchers(whites.toArray(new String[whites.size()]))
//                .permitAll()
//                .anyRequest()
//                .authenticated();
//        // 禁用缓存
//        http.headers().cacheControl();
//        http.headers().frameOptions().sameOrigin();
//    }

//    @Override
//    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
//        if (!StringUtils.isEmpty(resourceId)) {
//            resources.resourceId(resourceId);
//        }
//        super.configure(resources);
//    }
//
//    @Bean
//    public UserManager userManager(TokenStore tokenStore) {
//        return new UserManager(tokenStore);
//    }

}
