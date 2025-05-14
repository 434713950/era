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

package com.ourexists.era.oauth2.resource.permission;

import com.ourexists.era.framework.core.EraSystemHeader;
import com.ourexists.era.framework.core.constants.CommonConstant;
import com.ourexists.era.framework.core.constants.ResultMsgEnum;
import com.ourexists.era.framework.core.user.TenantInfo;
import com.ourexists.era.framework.core.user.UserContext;
import com.ourexists.era.framework.core.utils.EraStandardUtils;
import com.ourexists.era.oauth2.core.PathRule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author pengcheng
 * @version 1.1.0
 * @date 2022/12/2 10:42
 * @since 1.1.0
 */
@Slf4j
public class RegionHandlerInterceptor implements HandlerInterceptor {

    private final PermissionWhiteListProperties authWhiteListProperties;

    private final AntPathMatcher antPathMatcher;

    public RegionHandlerInterceptor(PermissionWhiteListProperties authWhiteListProperties) {
        this.authWhiteListProperties = authWhiteListProperties;
        this.antPathMatcher = new AntPathMatcher();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean ignore = false;
        for (String whiteList : authWhiteListProperties.getAuthCheck()) {
            if (antPathMatcher.match(whiteList, request.getServletPath())) {
                //白名单设置默认的
                ignore = true;
            }
        }
        //先默认获取请求头的租户信息用于白名单路径
        String platform = EraSystemHeader.extractPlatform(request);
        if (!StringUtils.hasText(platform) && !ignore) {
            EraStandardUtils.exceptionView(response, ResultMsgEnum.UNRECOGNIZED_HEADER, "unrecognized platform");
            return false;
        }
        UserContext.setPlatform(platform);
        TenantInfo tenantInfo = EraSystemHeader.extractTenant(request);
        if (tenantInfo == null) {
            if (!ignore) {
                EraStandardUtils.exceptionView(response, ResultMsgEnum.UNRECOGNIZED_HEADER, "unrecognized tenant");
                return false;
            } else {
                tenantInfo = new TenantInfo();
            }
        }
        if (antPathMatcher.match(PathRule.OVERALL_PREFIX + "/**", request.getServletPath())) {
            tenantInfo.setTenantId(CommonConstant.SYSTEM_TENANT);
        }
        if (antPathMatcher.match(PathRule.LIMIT_PREFIX + "/**", request.getServletPath())) {
            tenantInfo.setSkipMain(false);
        }
        UserContext.setTenant(tenantInfo);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.remove();
    }
}
