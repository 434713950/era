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

package com.ourexists.era.framework.core.model.vo;

import com.ourexists.era.framework.core.constants.ResultMsgEnum;
import lombok.Data;

/**
 * @author peng_cheng
 * 返回给客户端的统一外包json对象
 * @param <T>
 */
@Data
public class JsonResponseEntity<T> {

    /**
     * 反参code
     *
     * (0：系统内部错误；1：成功；2：外部错误；3：未登录；4：权限不足)
     */
    private int code;

    /**
     * 返回的消息
     */
    private String msg;

    /**
     * 返回的数据
     */
    private T data;

    private Pagination pagination;

    public JsonResponseEntity() {
    }

    public JsonResponseEntity(int code, String message, T data, Pagination pagination) {
        this(code, message);
        this.data = data;
        this.pagination = pagination;
    }

    public JsonResponseEntity(int code, String message) {
        this.code = code;
        this.msg = message;
    }

    public JsonResponseEntity(ResultMsgEnum resultMsgEnum) {
        this(resultMsgEnum.getResultCode(), resultMsgEnum.getResultMsg());
    }

    public JsonResponseEntity(ResultMsgEnum resultMsgEnum, T data, Pagination pagination) {
        this(resultMsgEnum);
        this.data = data;
        this.pagination = pagination;
    }

    /**
     * 返回成功信息
     * @return ResponseParam<K>
     */
    public static JsonResponseEntity<?> success() {
        return new JsonResponseEntity<>(ResultMsgEnum.SUCCESS);
    }

    /**
     * 返回成功信息
     * @param data        要携带的参数
     * @param <K>		携带的参数类型
     * @return ResponseParam<K>
     */
    public static <K> JsonResponseEntity<K> success(K data) {
        return new JsonResponseEntity<>(ResultMsgEnum.SUCCESS, data, null);
    }

    /**
     * 返回成功信息
     * @param data        要携带的参数
     * @param msg        要携带的信息
     * @param <K>		携带的参数类型
     * @return ResponseParam<K>
     */
    public static <K> JsonResponseEntity<K> success(K data, String msg) {
        return new JsonResponseEntity<>(ResultMsgEnum.SUCCESS.getResultCode(), msg, data, null);
    }

    /**
     * 返回成功信息
     * @param data                要携带的参数
     * @param pagination        要携带的分页信息
     * @param <K>				携带的参数类型
     * @return ResponseParam<K>
     */
    public static <K> JsonResponseEntity<K> success(K data, Pagination pagination) {
        return new JsonResponseEntity<>(ResultMsgEnum.SUCCESS, data, pagination);
    }

    /**
     * 返回系统代码错误信息（用于代码内部错误提示）
     * @return ResponseParam
     */
    public static JsonResponseEntity systemError() {
        return new JsonResponseEntity<>(ResultMsgEnum.SYSTEM_ERROR, null, null);
    }
}
