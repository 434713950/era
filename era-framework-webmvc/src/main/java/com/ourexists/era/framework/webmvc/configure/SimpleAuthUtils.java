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

package com.ourexists.era.framework.webmvc.configure;

import com.alibaba.fastjson.JSON;
import com.ourexists.era.framework.core.user.OperatorModel;
import com.ourexists.era.framework.core.user.TenantDataAuth;
import com.ourexists.era.framework.core.user.TenantInfo;
import com.ourexists.era.framework.core.user.UserInfo;
import com.ourexists.era.framework.core.utils.AuthUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @author pengcheng
 * @date 2022/4/8 17:25
 * @since 1.0.0
 */
@Slf4j
public class SimpleAuthUtils {

    public static final String USER_ROUTE = "x-route-user";

    /**
     * 租户权限路由头
     */
    public static final String TENANT_DATA_AUTH_ROUTE = "x-route-tenant-data-auth";

    public static final String TENANT_ROLE_HEADER = "x-tenant-role";

    public static final String TENANT_SKIPMAIN = "x-tenant-skipmain";

    public static final String REAL_TENANT_ROUTE = "x-route-tenant-id";

    public static String extractPlatform(HttpServletRequest request) {
        return AuthUtils.extractPlatform(request);
    }

    public static UserInfo extractUserInfo(HttpServletRequest request) {
        String userStr = request.getHeader(USER_ROUTE);
        try {
            if (StringUtils.isEmpty(userStr)) {
                return null;
            }
            String u = URLDecoder.decode(userStr, "UTF-8");
            return JSON.parseObject(u, UserInfo.class);
        } catch (UnsupportedEncodingException e) {
            //nothing;
            return null;
        }
    }

    public static UserInfo extractTenantRole(HttpServletRequest request) {
        String userStr = request.getHeader(USER_ROUTE);
        try {
            if (StringUtils.isEmpty(userStr)) {
                return null;
            }
            String u = URLDecoder.decode(userStr, "UTF-8");
            return JSON.parseObject(u, UserInfo.class);
        } catch (UnsupportedEncodingException e) {
            //nothing;
            return null;
        }
    }

    public static TenantInfo extractTenant(HttpServletRequest request) {
        String tenantId = request.getHeader(REAL_TENANT_ROUTE);
        if (StringUtils.isEmpty(tenantId)) {
            tenantId = AuthUtils.extractTenant(request);
        }
        if (StringUtils.isEmpty(tenantId)) {
            return null;
        }
        String tenantRole = request.getHeader(TENANT_ROLE_HEADER);
        TenantInfo tenantInfo = new TenantInfo()
                .setTenantId(tenantId)
                .setRole(tenantRole);
        String skim = request.getHeader(TENANT_SKIPMAIN);
        if (!StringUtils.isEmpty(skim)) {
            tenantInfo.setSkipMain(Boolean.parseBoolean(skim));
        }
        String r = request.getHeader(TENANT_DATA_AUTH_ROUTE);
        if (StringUtils.isEmpty(r)) {
            return tenantInfo;
        }
        TenantDataAuth tenantDataAuth = new TenantDataAuth();
        String[] operateNames = r.split(";");
        for (String operateName : operateNames) {
            tenantDataAuth.addLowControlPower(OperatorModel.valueOf(operateName));
        }
        tenantInfo.setTenantDataAuth(tenantDataAuth);
        return tenantInfo;
    }

    /**
     * 从header 请求中的clientId/clientSecret
     *
     * @param header header信息
     * @return 内容顺序依次为：clientId、
     */
    public static String[] extractAndDecodeHeader(String header) {
        return AuthUtils.extractAndDecodeHeader(header);
    }

    /**
     * 从请求中获取clientId/clientSecret
     *
     * @param request 请求
     * @return
     */
    public static String[] extractAndDecodeHeader(HttpServletRequest request) {
        return AuthUtils.extractAndDecodeHeader(request);
    }
}
