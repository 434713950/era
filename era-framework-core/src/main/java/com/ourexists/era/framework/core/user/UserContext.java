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

package com.ourexists.era.framework.core.user;

import com.ourexists.era.framework.core.constants.CommonConstant;

/**
 * @author pengcheng
 * @date 2020/4/23 23:15
 * @since 1.0.0
 */
public class UserContext {

    private static final ThreadLocal<UserInfo> USER_HOLDER = new ThreadLocal<>();

    private static final ThreadLocal<TenantInfo> TENANT = new ThreadLocal<>();

    private static final ThreadLocal<String> PLATFORM_HOLDER = new ThreadLocal<>();

    public static void setUser(UserInfo userInfo) {
        USER_HOLDER.set(userInfo);
    }

    public static TenantInfo getTenant() {
        return TENANT.get();
    }

    public static void setTenant(TenantInfo tenant) {
        TENANT.set(tenant);
    }

    public static UserInfo getUser() {
        return USER_HOLDER.get();
    }


    public static void setPlatform(String platform) {
        PLATFORM_HOLDER.set(platform);
    }

    public static String getPlatForm() {
        return PLATFORM_HOLDER.get();
    }

    public static void remove() {
        USER_HOLDER.remove();
        TENANT.remove();
        PLATFORM_HOLDER.remove();
    }

    public static void defaultHandle() {
        defaultTenant();
        defaultUser();
    }

    public static void defaultTenant() {
        UserContext.setTenant(new TenantInfo()
                .setTenantId(CommonConstant.SYSTEM_TENANT));
    }

    public static void defaultUser() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName("system");
        userInfo.setNickName("system");
        userInfo.setAccName("system");
        userInfo.setId("0");
        UserContext.setUser(userInfo);
    }

    /**
     * 清除数据权限
     */
    public static void clearDataAuth() {
        TenantInfo tenantInfo = UserContext.getTenant();
        if (tenantInfo != null) {
            tenantInfo.setTenantDataAuth(new TenantDataAuth());
        }
    }

    /**
     * 子数据权限设置
     */
    public static void subDataAuthSetUp() {
        TenantInfo tenantInfo = UserContext.getTenant();
        if (tenantInfo != null) {
            TenantDataAuth tenantDataAuth = new TenantDataAuth();
            tenantDataAuth.addLowControlPower(OperatorModel.QUERY);
            tenantDataAuth.addLowControlPower(OperatorModel.UPDATE);
            tenantDataAuth.addLowControlPower(OperatorModel.DELETE);
            tenantInfo.setTenantDataAuth(tenantDataAuth);
        }
    }

    /**
     * 父数据权限贯穿
     * @param level 层级，从最上级 1 开始
     */
    public static void parentDataAuthSetUp(int level) {
        TenantInfo tenantInfo = UserContext.getTenant();
        if (tenantInfo != null) {
            String tenantId = tenantInfo.getTenantId();
            if (tenantId.length() <= 3) {
                return;
            }
            tenantId = tenantId.substring(0, level * 3);
            tenantInfo.setTenantId(tenantId);
            subDataAuthSetUp();
        }
    }
}
