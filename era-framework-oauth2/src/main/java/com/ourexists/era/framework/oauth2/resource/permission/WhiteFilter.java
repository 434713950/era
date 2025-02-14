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

package com.ourexists.era.framework.oauth2.resource.permission;

import com.ourexists.era.framework.core.utils.AuthUtils;
import com.ourexists.era.framework.oauth2.AuthConstants;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.RequestFacade;
import org.apache.tomcat.util.http.MimeHeaders;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ReflectionUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pengcheng
 * @version 1.1.0
 * @date 2023/2/10 18:27
 * @since 1.1.0
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WhiteFilter extends HttpFilter {

    private final PermissionWhiteListProperties authWhiteListProperties;

    private final AntPathMatcher antPathMatcher;

    public WhiteFilter(PermissionWhiteListProperties authWhiteListProperties,
                       AntPathMatcher antPathMatcher) {
        this.authWhiteListProperties = authWhiteListProperties;
        this.antPathMatcher = antPathMatcher;
    }

    @SneakyThrows
    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        List<String> white = new ArrayList<>();
        white.addAll(AuthConstants.SYSTEM_WHITE_PATH);
        white.addAll(authWhiteListProperties.getAuthCheck());
        for (String whiteList : white) {
            if (antPathMatcher.match(whiteList, req.getServletPath())) {
                Field connectorField = ReflectionUtils.findField(RequestFacade.class, "request", Request.class);
                connectorField.setAccessible(true);
                Request connectorRequest = (Request) connectorField.get(req);

                Field coyoteField = ReflectionUtils.findField(Request.class, "coyoteRequest", org.apache.coyote.Request.class);
                coyoteField.setAccessible(true);
                org.apache.coyote.Request coyoteRequest = (org.apache.coyote.Request) coyoteField.get(connectorRequest);

                // 从 org.apache.coyote.Request 中获取 MimeHeaders
                Field mimeHeadersField =  ReflectionUtils.findField(org.apache.coyote.Request.class, "headers", MimeHeaders.class);
                mimeHeadersField.setAccessible(true);
                MimeHeaders mimeHeaders =  (MimeHeaders) mimeHeadersField.get(coyoteRequest);
                mimeHeaders.removeHeader(AuthUtils.AUTH_HEADER);
                mimeHeadersField.setAccessible(false);
                coyoteField.setAccessible(false);
                connectorField.setAccessible(false);
            }
        }
        filterChain.doFilter(req, servletResponse);
    }
}
