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

package com.ourexists.era.oauth2.core.interceptor;

import com.ourexists.era.framework.core.PathRule;
import com.ourexists.era.framework.core.constants.CommonConstant;
import com.ourexists.era.framework.core.constants.ResultMsgEnum;
import com.ourexists.era.framework.core.user.UserContext;
import com.ourexists.era.framework.core.utils.EraStandardUtils;
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

    private final AntPathMatcher antPathMatcher;

    private final PermissionWhiteListProperties permissionWhiteListProperties;

    public RegionHandlerInterceptor(AntPathMatcher antPathMatcher,
                                    PermissionWhiteListProperties permissionWhiteListProperties) {
        this.antPathMatcher = antPathMatcher;
        this.permissionWhiteListProperties = permissionWhiteListProperties;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean ignore = false;
        for (String whiteList : permissionWhiteListProperties.getAuthCheck()) {
            if (antPathMatcher.match(whiteList, request.getServletPath())) {
                //白名单设置默认的
                ignore = true;
            }
        }
        if (!StringUtils.hasText(UserContext.getPlatForm()) && !ignore) {
            EraStandardUtils.exceptionView(response, ResultMsgEnum.UNRECOGNIZED_HEADER, "unrecognized platform");
            return false;
        }
        if (UserContext.getTenant() == null) {
            if (!ignore) {
                EraStandardUtils.exceptionView(response, ResultMsgEnum.UNRECOGNIZED_HEADER, "unrecognized tenant");
                return false;
            } else {
                UserContext.defaultTenant();
            }
        }
        if (antPathMatcher.match(PathRule.OVERALL_PREFIX + "/**", request.getServletPath())) {
            UserContext.getTenant().setTenantId(CommonConstant.SYSTEM_TENANT);
        }
        if (antPathMatcher.match(PathRule.LIMIT_PREFIX + "/**", request.getServletPath())) {
            UserContext.getTenant().setSkipMain(false);
        }
        return true;
    }


}
