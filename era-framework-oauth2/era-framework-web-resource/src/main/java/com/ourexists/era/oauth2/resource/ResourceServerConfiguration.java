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

import com.ourexists.era.framework.core.PathRule;
import com.ourexists.era.oauth2.core.handler.EmptyEraAccessDeniedHandler;
import com.ourexists.era.oauth2.core.handler.EmptyEraAuthenticationEntryPoint;
import com.ourexists.era.oauth2.core.handler.EraAccessDeniedHandler;
import com.ourexists.era.oauth2.core.handler.EraAuthenticationEntryPoint;
import com.ourexists.era.oauth2.core.interceptor.HeaderHandlerInterceptor;
import com.ourexists.era.oauth2.core.interceptor.RegionHandlerInterceptor;
import com.ourexists.era.oauth2.core.store.PermissionStore;
import com.ourexists.era.oauth2.core.interceptor.PermissionHandlerInterceptor;
import com.ourexists.era.oauth2.core.interceptor.PermissionWhiteListProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pengcheng
 * @date 2022/4/13 23:23
 * @since 1.0.0
 */
@Configuration
@EnableWebSecurity
@Import({PermissionWhiteListProperties.class})
public class ResourceServerConfiguration implements WebMvcConfigurer {

//    @Value("${spring.application.name}")
//    private String resourceId;

    private final PermissionWhiteListProperties whiteListProperties;

    private final PermissionStore permissionStore;

    private final Environment env;

    private final AntPathMatcher antPathMatcher;

    public ResourceServerConfiguration(PermissionWhiteListProperties whiteListProperties,
                                       PermissionStore permissionStore,
                                       Environment env) {
        this.whiteListProperties = whiteListProperties;
        this.permissionStore = permissionStore;
        this.antPathMatcher = new AntPathMatcher();
        this.env = env;
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HeaderHandlerInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(PathRule.HERDER_WHITE_PATHS)
                .order(1);
        registry.addInterceptor(new RegionHandlerInterceptor(antPathMatcher, whiteListProperties))
                .addPathPatterns("/**")
                .excludePathPatterns(PathRule.HERDER_WHITE_PATHS)
                .order(2);
        registry.addInterceptor(new PermissionHandlerInterceptor(whiteListProperties, permissionStore, antPathMatcher, env))
                .addPathPatterns("/**")
                .excludePathPatterns(PathRule.SYSTEM_WHITE_PATH)
                .order(3);
    }

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
