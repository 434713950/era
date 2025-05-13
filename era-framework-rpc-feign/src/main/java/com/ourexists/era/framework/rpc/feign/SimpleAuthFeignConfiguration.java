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

import com.alibaba.fastjson2.JSON;
import com.ourexists.era.framework.core.user.OperatorModel;
import com.ourexists.era.framework.core.utils.CollectionUtil;
import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.List;

/**
 * @author pengcheng
 * @date 2021/4/12 17:03
 * @since 2.0.0
 */
@Slf4j
public class SimpleAuthFeignConfiguration {

    @Bean
    public RequestInterceptor requestInterceptor(SimpleAuthRequestManager manager) {
        return requestTemplate -> {
            if (log.isDebugEnabled()) {
                log.debug("applet server request start");
            }
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                Enumeration<String> headerNames = request.getHeaderNames();
                if (headerNames != null) {
                    while (headerNames.hasMoreElements()) {
                        String name = headerNames.nextElement();
                        String values = request.getHeader(name);
                        if (name.equals("content-length") || name.equals("x-tenant-role") || name.equals("x-route-user")) {
                            continue;
                        }
                        if (name.equals("x-route-tenant") && !StringUtils.isEmpty( manager.tenantId())) {
                            continue;
                        }
                        if (name.equals("x-era-platform") && !StringUtils.isEmpty(manager.platform())) {
                            continue;
                        }
                        requestTemplate.header(name, values);
                    }
                }
            }
            try {
                if (!StringUtils.isEmpty( manager.tenantId())) {
                    requestTemplate.header("x-route-tenant", manager.tenantId());
                }
                if (!StringUtils.isEmpty(manager.platform())) {
                    requestTemplate.header("x-era-platform", manager.platform());
                }
                requestTemplate.header("x-route-user", URLEncoder.encode(JSON.toJSONString(manager.userInfo()), "UTF-8"));
                requestTemplate.header("x-tenant-role", manager.tenantRole());
                requestTemplate.header("x-tenant-skipmain", manager.skipMain().toString());
                log.info("【feign请求】请求头[{}]",JSON.toJSONString(requestTemplate.headers()));
                List<OperatorModel> operatorModels = manager.tenantDataAuth();
                if (CollectionUtil.isNotBlank(operatorModels)) {
                    StringBuilder t = new StringBuilder();
                    for (OperatorModel operatorModel : operatorModels) {
                        t.append(operatorModel.name()).append(";");
                    }
                    requestTemplate.header("x-route-tenant-data-auth", t.substring(0, t.length() - 1));
                }
            } catch (UnsupportedEncodingException e) {
                //nothing
            }
        };
    }
}
