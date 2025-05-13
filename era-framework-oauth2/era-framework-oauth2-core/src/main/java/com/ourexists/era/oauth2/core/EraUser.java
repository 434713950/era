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

package com.ourexists.era.oauth2.core;

import com.ourexists.era.framework.core.user.TenantInfo;
import com.ourexists.era.framework.core.user.UserInfo;
import com.ourexists.era.oauth2.core.authority.ApiPermission;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Map;

/**
 * @author pengcheng
 * @date 2022/4/14 15:52
 * @since 1.0.0
 */
public class EraUser extends User {

    private static final long serialVersionUID = -3380677573884717051L;

    private final Map<String, TenantInfo> tenantInfoMap;

    private UserInfo userInfo;

    private Integer accStatus;

    private final Collection<? extends ApiPermission> apiPermissions;

    public EraUser(UserInfo userInfo, String password, Map<String, TenantInfo> tenantInfoMap,
                   Collection<? extends ApiPermission> apiPermissions,
                   Collection<? extends GrantedAuthority> authorities) {
        super(userInfo.getAccName(), password, authorities);
        this.tenantInfoMap = tenantInfoMap;
        this.userInfo = userInfo;
        this.apiPermissions = apiPermissions;
    }

    public EraUser(UserInfo userInfo, String password, boolean enabled, boolean accountNonExpired,
                   boolean credentialsNonExpired, boolean accountNonLocked, Map<String, TenantInfo> tenantInfoMap,
                   Collection<? extends ApiPermission> apiPermissions,
                   Collection<? extends GrantedAuthority> authorities) {
        super(userInfo.getAccName(), password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.tenantInfoMap = tenantInfoMap;
        this.userInfo = userInfo;
        this.apiPermissions = apiPermissions;
    }

    public Map<String, TenantInfo> getTenantInfoMap() {
        return tenantInfoMap;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public Collection<? extends ApiPermission> getApiPermissions() {
        return apiPermissions;
    }

    public Integer getAccStatus() {
        return accStatus;
    }

    public void setAccStatus(Integer accStatus) {
        this.accStatus = accStatus;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
