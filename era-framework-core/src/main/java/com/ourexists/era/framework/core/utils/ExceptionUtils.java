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

import com.google.common.base.Strings;

/**
 * @author pengcheng
 * @date 2022/7/7 14:27
 * @since 1.0.0
 */
public class ExceptionUtils {

    /**
     * 主键/唯一键冲突的异常信息
     */
    public static final String UNIQUE_ERROR = "Duplicate entry";

    private ExceptionUtils(){}

    /**
     * 获取到错误描述
     */
    public static String getErrorCode(Exception e) {
        String errorCode = "";
        if (e.getCause() != null && e.getCause().getMessage() != null) {
            errorCode = e.getCause().getMessage();
        }
        if (Strings.isNullOrEmpty(errorCode) && e.getMessage() != null) {
            errorCode = e.getMessage();
        }
        return errorCode;
    }
}
