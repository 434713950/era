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

package com.ourexists.era.framework.core.constants;

import lombok.Getter;

/**
 * 返回的响应码集合
 *
 * @author PengCheng
 * @date 2018/12/12
 */
public enum ResultMsgEnum {
    /**
     *
     */
    SUCCESS(200,"操作成功"),
    SYSTEM_ERROR(500,"系统错误,请联系管理员!"),
    VALIDATION_ERROR(406,"参数校验失败!"),
    PERMISSION_DENIED(405,"您没有访问权限!"),

    UNRECOGNIZED_HEADER(417, "无法辨识的请求头"),
    INIT_PASS(20,"当前密码为初始密码,请修改!"),
    UN_LOGIN(403,"未登录");

    @Getter
    private final Integer resultCode;

    @Getter
    private final String resultMsg;

    ResultMsgEnum(Integer resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }
}
