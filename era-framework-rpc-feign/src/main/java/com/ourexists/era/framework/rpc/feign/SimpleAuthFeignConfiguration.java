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
import com.ourexists.era.framework.core.EraSystemHeader;
import com.ourexists.era.framework.core.constants.CommonConstant;
import com.ourexists.era.framework.core.user.OperatorModel;
import com.ourexists.era.framework.core.utils.CollectionUtil;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * @author pengcheng
 * {@code @date 2021/4/12 17:03}
 * @since 2.0.0
 */
@Slf4j
public class SimpleAuthFeignConfiguration {

    @Bean
    public RequestInterceptor requestInterceptor(EraFeignHeaderTransfer transfer) {
        return requestTemplate -> {
            if (log.isDebugEnabled()) {
                log.debug("applet server request start");
            }
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes();
            if (attributes != null) {
                Map<String, String> headerNames = transfer.requestHeaders();
                if (headerNames != null) {
                    for (Map.Entry<String, String> headers : headerNames.entrySet()) {
                        String name = headers.getKey();
                        String value = headers.getValue();
                        if (name.equals("content-length")
                                || name.equals(EraSystemHeader.AUTH_CONTRO_ROLE_HEADER)
                                || name.equals(EraSystemHeader.AUTH_CONTRO_USER_HEADER)
                                || (name.equals(EraSystemHeader.TENANT_ROUTE) && StringUtils.hasText(transfer.tenantId()))
                                || (name.equals(EraSystemHeader.PLATFORM_HEADER) && StringUtils.hasText(transfer.platform()))
                        ) {
                            continue;
                        }
                        requestTemplate.header(name, value);
                    }
                }
            }
            try {
                if (StringUtils.hasText(transfer.tenantId())) {
                    requestTemplate.header(EraSystemHeader.TENANT_ROUTE, transfer.tenantId());
                }
                if (StringUtils.hasText(transfer.platform())) {
                    requestTemplate.header(EraSystemHeader.PLATFORM_HEADER, transfer.platform());
                }
                requestTemplate.header(EraSystemHeader.AUTH_CONTRO_USER_HEADER, URLEncoder.encode(JSON.toJSONString(transfer.userInfo()), CommonConstant.CONTENT_ENCODE));
                requestTemplate.header(EraSystemHeader.AUTH_CONTRO_ROLE_HEADER, transfer.tenantRole());
                requestTemplate.header(EraSystemHeader.AUTH_CONTRO_SKIPMAIN, transfer.skipMain().toString());
                log.debug("【feign请求】请求头[{}]", JSON.toJSONString(requestTemplate.headers()));
                List<OperatorModel> operatorModels = transfer.tenantDataAuth();
                if (CollectionUtil.isNotBlank(operatorModels)) {
                    StringBuilder t = new StringBuilder();
                    for (OperatorModel operatorModel : operatorModels) {
                        t.append(operatorModel.name()).append(";");
                    }
                    requestTemplate.header(EraSystemHeader.AUTH_CONTRO_DATA_AUTH_HEADER, t.substring(0, t.length() - 1));
                }
            } catch (UnsupportedEncodingException e) {
                //nothing
            }
        };
    }
}
