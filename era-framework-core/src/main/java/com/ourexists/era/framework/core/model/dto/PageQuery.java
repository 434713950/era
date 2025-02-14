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

package com.ourexists.era.framework.core.model.dto;

import lombok.Data;

/**
 * @Author: PengCheng
 * @Description: 分页查询基础query
 * @Date: 15:23 2018/4/10/010
 */
@Data
public class PageQuery extends BaseDto {

    private Integer page = 1;

    private Integer pageSize = 10;

    private Boolean requirePage = true;

    /**
     * 该方式用于获取sql中额limit参数
     * @return
     */
    public Integer getLimitPage(){
        return  (this.page-1)*pageSize;
    }


    public Integer getPage() {
        if (this.requirePage != null && !requirePage) {
            return 1;
        }
        return page;
    }


    public Integer getPageSize() {
        if (this.requirePage != null && !requirePage) {
            return Integer.MAX_VALUE;
        }
        return pageSize;
    }
}
