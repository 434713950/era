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

package com.ourexists.era.framework.core.utils.excel;


import com.ourexists.era.framework.core.exceptions.ImportExportException;

/**
 * @author pengcheng
 * @date 2021/9/1 14:25
 * @since 1.0.0
 */
public interface ImportChecker<T extends BaseExcelModel> {

    /**
     * 校验model参数
     * @param importModel   导入的model
     * @throws ImportExportException    校验失败的通过异常将失败信息抛出
     */
    void check(T importModel) throws ImportExportException;
}
