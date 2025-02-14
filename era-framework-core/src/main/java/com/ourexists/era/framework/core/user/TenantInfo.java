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

import java.io.Serializable;

/**
 * @author pengcheng
 * @date 2022/5/12 15:28
 * @since 1.0.0
 */
public class TenantInfo implements Serializable {

    private static final long serialVersionUID = 8792408769158215704L;

    private String tenantId;

    private String role;

    private Integer managementControl;

    private Boolean skipMain = true;

    private TenantDataAuth tenantDataAuth;

    /**
     * 是否避免租户条件
     *
     * @return
     */
    public Boolean avoidTenantCondition() {
        return CommonConstant.SYSTEM_TENANT.equals(this.tenantId) && (this.skipMain == null || this.skipMain);
    }

    public String getTenantId() {
        return tenantId;
    }

    public TenantInfo setTenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public String getRole() {
        return role;
    }

    public TenantInfo setRole(String role) {
        this.role = role;
        return this;
    }

    public Boolean getSkipMain() {
        return skipMain;
    }

    public TenantInfo setSkipMain(Boolean skipMain) {
        this.skipMain = skipMain;
        return this;
    }

    public TenantDataAuth getTenantDataAuth() {
        return tenantDataAuth;
    }

    public void setTenantDataAuth(TenantDataAuth tenantDataAuth) {
        this.tenantDataAuth = tenantDataAuth;
    }

    public Integer getManagementControl() {
        return managementControl;
    }

    public TenantInfo setManagementControl(Integer managementControl) {
        this.managementControl = managementControl;
        return this;
    }
}
