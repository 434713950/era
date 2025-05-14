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

import com.ourexists.era.framework.core.EraSystemHeader;
import com.ourexists.era.framework.core.user.UserContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;


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
        UserContext.setPlatform(EraSystemHeader.extractPlatform(request));
        UserContext.setTenant(EraSystemHeader.extractTenant(request));
        UserContext.setUser(EraSystemHeader.extractUserInfo(request));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.remove();
    }
}
