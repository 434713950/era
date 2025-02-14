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


/**
 * Eras数据接收权限
 *
 * @author pengcheng
 * @date 2022/7/25 17:21
 * @since 1.1.0
 */
public class EraDataAccessAuth {

    /**
     * 检测是有拥有相应租户的控制权
     * @param operatorModel 控制操作
     * @return
     */
    public static boolean checkTenantControlPower(OperatorModel operatorModel) {
        TenantInfo tenantInfo = UserContext.getTenant();
        if (tenantInfo == null) {
            return false;
        }
        TenantDataAuth tenantDataAuth = tenantInfo.getTenantDataAuth();
        if (tenantDataAuth == null) {
            return false;
        }
        return tenantDataAuth.checkControlPower(operatorModel);
    }
}
