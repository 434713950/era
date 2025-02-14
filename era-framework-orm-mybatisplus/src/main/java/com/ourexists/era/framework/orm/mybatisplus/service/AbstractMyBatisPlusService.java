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

package com.ourexists.era.framework.orm.mybatisplus.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * <p>mybatis-plus使用的service模板</p>
 *
 * @author PengCheng
 * @date 15:33 2018/4/10/010
 */
public abstract class AbstractMyBatisPlusService<M extends BaseMapper<T>, T> extends ServiceImpl<M,T> implements IMyBatisPlusService<T> {

    public T getOne(QueryWrapper<T> queryWrapper) {
        queryWrapper.last("limt 1");
        return super.getOne(queryWrapper);
    }
}
