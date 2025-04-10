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

package com.ourexists.era.framework.core.exceptions;

import com.ourexists.era.framework.core.constants.ResultMsgEnum;
import lombok.Data;

/**
 * @author pengCheng
 * 请求出错异常
 */
@Data
public class BusinessException extends RuntimeException {

    private Integer code;

    private String msg;

    private String[] text;

    /**
     * 是否要刷新页面
     */
    private Boolean isRefresh;

    public BusinessException(Integer code, String msg, String... text) {
        this(code, false, msg, text);
    }

    public BusinessException(String msg, String... text) {
        this(ResultMsgEnum.SYSTEM_ERROR.getResultCode(), false, msg, text);
    }

    public BusinessException(Integer code, Boolean isRefresh, String msg, String... text) {
        this.code = code;
        this.isRefresh = isRefresh;
        this.msg = msg;
        this.text = text;
    }

    @Override
    public String getMessage() {
        return this.msg;
    }
}
