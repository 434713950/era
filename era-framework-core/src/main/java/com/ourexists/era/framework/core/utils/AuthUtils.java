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

package com.ourexists.era.framework.core.utils;

import com.ourexists.era.framework.core.constants.CommonConstant;
import com.ourexists.era.framework.core.exceptions.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Base64;

/**
 * @author pengcheng
 * @date 2022/4/8 17:25
 * @since 1.0.0
 */
@Slf4j
public class AuthUtils {

    /**
     * 认证头信息前缀
     */
    public static final String AUTH_HEADER_PREFIX = "era_";

    /**
     * 认证信息的定义边界符
     */
    public static final String AUTH_INFO_DELIMIT = ":";

    /**
     * 认证头
     */
    public static final String AUTH_HEADER = "Authorization";

    /**
     * 租户路由头
     */
    public static final String TENANT_ROUTE = "x-route-tenant";


    public static final String PLATFORM_HEADER = "x-era-platform";


    public static String extractPlatform(HttpServletRequest request) {
        String platform = request.getHeader(PLATFORM_HEADER);
        return StringUtils.isEmpty(platform) ? "YLOMS" : platform;
    }


    public static String extractTenant(HttpServletRequest request) {

        return request.getHeader(TENANT_ROUTE);
    }

    /**
     * 从header 请求中的clientId/clientSecret
     *
     * @param header header信息
     * @return 内容顺序依次为：clientId、
     */
    public static String[] extractAndDecodeHeader(String header) {
        String headerInfo = header.substring(AUTH_HEADER_PREFIX.length());
        byte[] decoded = Base64.getDecoder().decode(headerInfo);

        try {
            String token = new String(decoded, CommonConstant.CONTENT_ENCODE);
            int delimit = token.indexOf(AUTH_INFO_DELIMIT);

            if (delimit != -1) {
                return new String[]{token.substring(0, delimit), token.substring(delimit + 1)};
            }
        } catch (Exception e) {
            log.error("Failed to decode authentication authorization\r\n", e);
            throw new BusinessException("Failed to decode authentication authorization");
        }
        return null;
    }

    /**
     * 从请求中获取clientId/clientSecret
     *
     * @param request 请求
     * @return
     */
    public static String[] extractAndDecodeHeader(HttpServletRequest request) {
        String header = request.getHeader(AUTH_HEADER);

        if (header == null || header.startsWith(AUTH_HEADER_PREFIX)) {
            throw new BusinessException(
                    "Failed to decode authentication authorization");
        }
        return extractAndDecodeHeader(header);
    }


}
