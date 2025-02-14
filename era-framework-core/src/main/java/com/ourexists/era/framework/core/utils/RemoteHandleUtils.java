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

package com.ourexists.era.framework.core.utils;

import com.ourexists.era.framework.core.exceptions.EraCommonException;
import com.ourexists.era.framework.core.constants.ResultMsgEnum;
import com.ourexists.era.framework.core.model.vo.JsonResponseEntity;

import java.util.Optional;

/**
 * 统一规范远程调用反参解析工具
 *
 * @author pengcheng
 * @date 2020/5/11 11:19
 */
public class RemoteHandleUtils {

    public static <T> T getDataFormResponse(JsonResponseEntity<T> resp) throws EraCommonException {
        if (resp.getCode() != ResultMsgEnum.SUCCESS.getResultCode()) {
            throw new EraCommonException(resp.getCode(), Optional.ofNullable(resp.getMsg()).orElse("操作失败!"));
        }
        return resp.getData();
    }
}
