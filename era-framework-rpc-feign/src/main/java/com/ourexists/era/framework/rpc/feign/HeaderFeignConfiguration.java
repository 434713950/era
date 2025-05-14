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

package com.ourexists.era.framework.rpc.feign;

import com.ourexists.era.framework.core.EraSystemHeader;
import com.ourexists.era.framework.core.user.UserContext;
import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Enumeration;
import java.util.Objects;

/**
 * @author pengcheng
 * @date 2022/5/31 14:59
 * @since 1.0.0
 */
public class HeaderFeignConfiguration {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes();
            if (Objects.isNull(attributes)) {
                return;
            }
            HttpServletRequest request = attributes.getRequest();
            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String name = headerNames.nextElement();
                    String values = request.getHeader(name);
                    if (name.equals("content-length")) {
                        continue;
                    }
                    requestTemplate.header(name, values);
                }
            }
            if (UserContext.getTenant() != null) {
                requestTemplate.header(EraSystemHeader.TENANT_ROUTE, UserContext.getTenant().getTenantId());
            }
            if (StringUtils.hasText(UserContext.getPlatForm())) {
                requestTemplate.header(EraSystemHeader.PLATFORM_HEADER, UserContext.getPlatForm());
            }
        };
    }
}
