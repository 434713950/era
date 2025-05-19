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

package com.ourexists.era.framework.rpc.feign;

import com.ourexists.era.framework.core.user.OperatorModel;
import com.ourexists.era.framework.core.user.UserInfo;

import java.util.List;
import java.util.Map;

/**
 * era生态的feign请求头传递
 *
 * @author pengcheng
 * @date 2021/4/12 17:04
 * @since 2.0.0
 */
public interface EraFeignHeaderTransfer {

    String tenantId();

    Map<String, String> requestHeaders();

    default String tenantRole() {
        return "COMMON";
    }

    UserInfo userInfo();

    default String platform() {
        return "UNKNOW";
    }


    default Boolean skipMain() {
        return true;
    }

    List<OperatorModel> tenantDataAuth();
}
