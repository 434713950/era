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

import lombok.Data;

import java.io.Serializable;

/**
 * <p>分页数据包装体</p>
 *
 * 返回用的分页信息实体
 * @author PengCheng
 * @date 2018/4/13
 */
@Data
public class Pagination implements Serializable {

    private static final long serialVersionUID = -3027439529245973552L;
    /**
     * 总数
     */
    private Integer total;

    /**
     * 当前页
     */
    private Integer current;

    /**
     * 每页展示数
     */
    private Integer pageSize;

    public Pagination() {
    }

    /**
     *
     * @param total         数据总数
     * @param current       当前页码
     * @param pageSize      每页尺寸
     * @param isIncrement   是否对当前页码进行+1返回
     */
    public Pagination(Integer total, Integer current, Integer pageSize,Boolean isIncrement) {
        this.total = total;
        if (isIncrement) {
            this.current = current + 1;
        }else {
            this.current = current;
        }
        this.pageSize = pageSize;
    }

    public Pagination(Integer total, int current, int size) {
        this(total,current,size,false);
    }
}
