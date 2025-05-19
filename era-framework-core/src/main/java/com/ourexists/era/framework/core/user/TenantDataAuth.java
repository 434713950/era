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

import com.ourexists.era.framework.core.utils.CollectionUtil;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author pengcheng
 * @date 2022/7/25 17:16
 * @since 1.1.0
 */
@Getter
public class TenantDataAuth {

    /**
     * 租户编号的层级分割规则长度
     */
    public static final Integer TENANT_LEVEL_LEN = 3;

    /**
     * 下级操作权
     */
    private final List<OperatorModel> lowerControlPower = Collections.synchronizedList(new ArrayList<>());

    public void addLowControlPower(OperatorModel operatorModel) {
        this.lowerControlPower.add(operatorModel);
    }

    /**
     * 检测是有拥有相应的控制权
     * @param operatorModel 控制操作
     * @return
     */
    public boolean checkControlPower(OperatorModel operatorModel) {
        if (CollectionUtil.isBlank(this.lowerControlPower)) {
            return false;
        }
        for (OperatorModel model : this.lowerControlPower) {
            if (model.equals(operatorModel)) {
                return true;
            }
        }
        return false;
    }
}
