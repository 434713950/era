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

package com.ourexists.era.framework.oauth2.authority;

import java.io.Serializable;
import java.util.List;

/**
 * era权限
 *
 * @author pengcheng
 * @date 2022/4/12 21:44
 * @since 1.0.0
 */
public class EraUserAuthority implements Serializable {

    private static final long serialVersionUID = 8643257777693126858L;

    private List<ApiPermission> apiPermissionList;

    public List<ApiPermission> getApiPermissionList() {
        return apiPermissionList;
    }

    public void setApiPermissionList(List<ApiPermission> apiPermissionList) {
        this.apiPermissionList = apiPermissionList;
    }
}
