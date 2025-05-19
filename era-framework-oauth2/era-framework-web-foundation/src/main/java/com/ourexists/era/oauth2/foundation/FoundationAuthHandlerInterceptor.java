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

package com.ourexists.era.oauth2.foundation;

import com.ourexists.era.framework.core.constants.CommonConstant;
import com.ourexists.era.framework.core.constants.ResultMsgEnum;
import com.ourexists.era.framework.core.user.TenantInfo;
import com.ourexists.era.framework.core.user.UserContext;
import com.ourexists.era.framework.core.user.UserInfo;
import com.ourexists.era.framework.core.utils.EraStandardUtils;
import com.ourexists.era.framework.core.PathRule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 权限认证拦截器
 *
 * @author pengcheng
 * @date 2022/4/12 22:21
 * @since 1.0.0
 */
@Slf4j
public class FoundationAuthHandlerInterceptor implements HandlerInterceptor {

    private final FoundationAuthWhiteListProperties foundationAuthWhiteListProperties;

    private final AntPathMatcher antPathMatcher;

    public FoundationAuthHandlerInterceptor(FoundationAuthWhiteListProperties foundationAuthWhiteListProperties,
                                            AntPathMatcher antPathMatcher) {
        this.foundationAuthWhiteListProperties = foundationAuthWhiteListProperties;
        this.antPathMatcher = antPathMatcher;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        //先默认获取请求头的租户信息用于白名单路径
        String path = request.getServletPath();
        for (String s : foundationAuthWhiteListProperties.getAuthCheck()) {
            if (antPathMatcher.match(s, path)) {
                return true;
            }
        }
        //租户信息必须
        if (UserContext.getTenant() == null) {
            EraStandardUtils.exceptionView(response, ResultMsgEnum.PERMISSION_DENIED);
            return false;
        }
        TenantInfo tenantInfo = UserContext.getTenant();
        if (antPathMatcher.match(PathRule.OVERALL_PREFIX + "/**", request.getServletPath())) {
            tenantInfo.setTenantId(CommonConstant.SYSTEM_TENANT);
        }
        if (antPathMatcher.match(PathRule.LIMIT_PREFIX + "/**", request.getServletPath())) {
            tenantInfo.setSkipMain(false);
        }
        if (UserContext.getUser() == null) {
            UserInfo userInfo = new UserInfo();
            userInfo.setId(CommonConstant.SYSTEM_TENANT);
            userInfo.setAccName("UNKNOW");
            userInfo.setUsername("UNKNOW");
            userInfo.setNickName("UNKNOW");
            UserContext.setUser(userInfo);
        }
        return true;
    }
}
