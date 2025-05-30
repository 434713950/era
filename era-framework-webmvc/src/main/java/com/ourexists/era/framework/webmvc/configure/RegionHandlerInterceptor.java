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

import com.ourexists.era.framework.core.user.UserContext;
import com.ourexists.era.framework.core.utils.AuthUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author pengcheng
 * @version 1.1.0
 * @date 2022/12/2 10:42
 * @since 1.1.0
 */
@Slf4j
public class RegionHandlerInterceptor implements HandlerInterceptor {

    public RegionHandlerInterceptor() {
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //先默认获取请求头的租户信息用于白名单路径
        UserContext.setPlatform(AuthUtils.extractPlatform(request));
        UserContext.setTenant(SimpleAuthUtils.extractTenant(request));
        UserContext.setUser(SimpleAuthUtils.extractUserInfo(request));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.remove();
    }
}
