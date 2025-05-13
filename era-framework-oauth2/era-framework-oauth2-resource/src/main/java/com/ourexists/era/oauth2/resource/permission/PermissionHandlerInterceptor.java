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

import com.alibaba.fastjson.JSONObject;
import com.ourexists.era.framework.core.constants.CommonConstant;
import com.ourexists.era.framework.core.constants.ResultMsgEnum;
import com.ourexists.era.framework.core.model.vo.JsonResponseEntity;
import com.ourexists.era.framework.core.user.TenantInfo;
import com.ourexists.era.framework.core.user.UserContext;
import com.ourexists.era.framework.core.utils.AuthUtils;
import com.ourexists.era.framework.core.utils.CollectionUtil;
import com.ourexists.era.oauth2.core.EraUser;
import com.ourexists.era.oauth2.core.authority.ApiPermission;
import com.ourexists.era.oauth2.resource.permission.store.PermissionStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

/**
 * 权限认证拦截器
 *
 * @author pengcheng
 * @date 2022/4/12 22:21
 * @since 1.0.0
 */
@Slf4j
public class PermissionHandlerInterceptor implements HandlerInterceptor {

    private final PermissionWhiteListProperties authWhiteListProperties;

    private final AntPathMatcher antPathMatcher;

    private final String serverName;

    private final PermissionStore permissionStore;

    public PermissionHandlerInterceptor(PermissionWhiteListProperties authWhiteListProperties,
                                        PermissionStore permissionStore,
                                        Environment env) {
        this.authWhiteListProperties = authWhiteListProperties;
        this.antPathMatcher = new AntPathMatcher();
        this.permissionStore = permissionStore;
        this.serverName = env.getProperty("spring.application.name");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        for (String whiteList : authWhiteListProperties.getAuthCheck()) {
            if (antPathMatcher.match(whiteList, request.getServletPath())) {
                //白名单设置默认的
                return true;
            }
        }
        for (String whiteList : authWhiteListProperties.getUserCheck()) {
            if (antPathMatcher.match(whiteList, request.getServletPath())) {
                //白名单设置默认的
                return true;
            }
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof EraAuthenticationToken) {
            Object principal = authentication.getPrincipal();
            if (!(principal instanceof EraUser)) {
                output(response, ResultMsgEnum.UN_LOGIN);
                return false;
            }
            EraUser eraUser = (EraUser) principal;
            UserContext.setUser(eraUser.getUserInfo());

            String tenantId = AuthUtils.extractTenant(request);
            if (StringUtils.isEmpty(tenantId)) {
                output(response, ResultMsgEnum.PERMISSION_DENIED);
                return false;
            }

            //api权限白名单
            for (String tenantCheck : authWhiteListProperties.getTenantCheck()) {
                if (antPathMatcher.match(tenantCheck, request.getServletPath())) {
                    return true;
                }
            }

            TenantInfo tenantInfo = eraUser.getTenantInfoMap().get(tenantId);
            if (tenantInfo == null) {
                output(response, ResultMsgEnum.PERMISSION_DENIED);
                return false;
            }
            UserContext.setTenant(tenantInfo);

            //租戶管理员不需要判断api权限
            if (AccRole.ADMIN.name().equals(tenantInfo.getRole())) {
                return true;
            }

            //api权限白名单
            for (String authCheck : authWhiteListProperties.getApiCheck()) {
                if (antPathMatcher.match(authCheck, request.getServletPath())) {
                    return true;
                }
            }
            Collection<? extends ApiPermission> allApiPermissions = this.permissionStore.apiPermissions();
            if (!checkApiPermission(request,
                    allApiPermissions,
                    eraUser.getApiPermissions())) {
                output(response, ResultMsgEnum.PERMISSION_DENIED);
                return false;
            }
        }
        return true;
    }


    private void output(HttpServletResponse response, ResultMsgEnum resultMsgEnum) {
        JsonResponseEntity jo = new JsonResponseEntity(resultMsgEnum.getResultCode(), resultMsgEnum.getResultMsg());
        String json = JSONObject.toJSONString(jo);
        response.setContentType(CommonConstant.CONTENT_TYPE);
        try (PrintWriter out = response.getWriter()) {
            out.write(json);
            out.flush();
        } catch (IOException e) {
            log.error("PermissionHandlerInterceptor output error", e);
        }
    }


    private boolean checkApiPermission(HttpServletRequest request,
                                       Collection<? extends ApiPermission> allApiPermissions,
                                       Collection<? extends ApiPermission> ownerApiPermissions) {
        return checkApiPermission(request.getServletPath(), allApiPermissions, ownerApiPermissions);
    }

    /**
     * 检测api权限
     *
     * @param requestPath         请求路径
     * @param allApiPermissions   所有有权限限制的api
     * @param ownerApiPermissions 用户持有的api权限
     * @return
     */
    private boolean checkApiPermission(String requestPath,
                                       Collection<? extends ApiPermission> allApiPermissions,
                                       Collection<? extends ApiPermission> ownerApiPermissions) {
        if (CollectionUtil.isBlank(allApiPermissions)) {
            return true;
        }
        boolean needValid = false;
        for (ApiPermission allApiPermission : allApiPermissions) {
            if (allApiPermission.getServerName().equals(this.serverName)
                    && antPathMatcher.match(allApiPermission.getPathPattern(), requestPath)) {
                needValid = true;
                break;
            }
        }

        if (!needValid) {
            return true;
        }
        for (ApiPermission ownerApiPermission : ownerApiPermissions) {
            if (ownerApiPermission.getServerName().equals(this.serverName)
                    && antPathMatcher.match(ownerApiPermission.getPathPattern(), requestPath)) {
                return true;
            }
        }
        return false;
    }
}
