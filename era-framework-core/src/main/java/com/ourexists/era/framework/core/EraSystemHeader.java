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

package com.ourexists.era.framework.core;

import com.alibaba.fastjson2.JSON;
import com.ourexists.era.framework.core.constants.CommonConstant;
import com.ourexists.era.framework.core.exceptions.BusinessException;
import com.ourexists.era.framework.core.user.OperatorModel;
import com.ourexists.era.framework.core.user.TenantDataAuth;
import com.ourexists.era.framework.core.user.TenantInfo;
import com.ourexists.era.framework.core.user.UserInfo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Base64;

/**
 * @author pengcheng
 * @date 2022/4/8 17:25
 * @since 1.0.0
 */
@Slf4j
public class EraSystemHeader {

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

    /**
     * 真实路由头
     */
    public static final String REAL_TENANT_ROUTE = "x-route-tenant-id";

    /**
     * 平台路由头
     */
    public static final String PLATFORM_HEADER = "x-era-platform";


    /**
     * 内部流转用户信息头
     */
    public static final String AUTH_CONTRO_USER_HEADER = "x-route-user";

    /**
     * 内部流转用户数据权限头
     */
    public static final String AUTH_CONTRO_DATA_AUTH_HEADER = "x-route-tenant-data-auth";

    /**
     *  内部流转用户角色权限头
     */
    public static final String AUTH_CONTRO_ROLE_HEADER = "x-tenant-role";

    /**
     * 内部流转隔离主租户信息头
     */
    public static final String AUTH_CONTRO_SKIPMAIN = "x-tenant-skipmain";


    public static String extractPlatform(HttpServletRequest request) {
        return request.getHeader(PLATFORM_HEADER);
    }

    public static UserInfo extractUserInfo(HttpServletRequest request) {
        String userStr = request.getHeader(AUTH_CONTRO_USER_HEADER);
        try {
            if (!StringUtils.hasText(userStr)) {
                return null;
            }
            String u = URLDecoder.decode(userStr, CommonConstant.CONTENT_ENCODE);
            return JSON.parseObject(u, UserInfo.class);
        } catch (UnsupportedEncodingException e) {
            //nothing;
            return null;
        }
    }

    public static String extractTenantId(HttpServletRequest request) {
        String tenantId = request.getHeader(REAL_TENANT_ROUTE);
        if (!StringUtils.hasText(tenantId)) {
            tenantId = request.getHeader(TENANT_ROUTE);
        }
        return tenantId;
    }

    public static TenantInfo extractTenant(HttpServletRequest request) {
        String tenantId = extractTenantId(request);
        if (tenantId == null) {
            return null;
        }
        TenantInfo tenantInfo = new TenantInfo()
                .setTenantId(tenantId)
                .setRole(request.getHeader(AUTH_CONTRO_ROLE_HEADER));
        String skim = request.getHeader(AUTH_CONTRO_SKIPMAIN);
        if (StringUtils.hasText(skim)) {
            tenantInfo.setSkipMain(Boolean.parseBoolean(skim));
        }
        String r = request.getHeader(AUTH_CONTRO_DATA_AUTH_HEADER);
        if (StringUtils.hasText(r)) {
            TenantDataAuth tenantDataAuth = new TenantDataAuth();
            String[] operateNames = r.split(";");
            for (String operateName : operateNames) {
                tenantDataAuth.addLowControlPower(OperatorModel.valueOf(operateName));
            }
            tenantInfo.setTenantDataAuth(tenantDataAuth);
        }
        return tenantInfo;
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
