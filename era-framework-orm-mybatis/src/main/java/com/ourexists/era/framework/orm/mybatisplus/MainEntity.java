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

package com.ourexists.era.framework.orm.mybatisplus;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 主表实体
 * @author pengcheng
 * @date 2020/4/25 17:45
 */
@Getter
@Setter
public class MainEntity implements Serializable {

    @TableId
    protected String id;

    @Version
    protected Integer revision;

    @TableField(fill= FieldFill.INSERT)
    protected String createdBy;

    @TableField(fill= FieldFill.INSERT)
    protected String createdId;

    @TableField(fill= FieldFill.INSERT)
    protected Date createdTime;

    @TableField(fill= FieldFill.INSERT_UPDATE)
    protected String updatedBy;

    @TableField(fill= FieldFill.INSERT_UPDATE)
    protected String updatedId;

    @TableField(fill= FieldFill.INSERT_UPDATE)
    protected Date updatedTime;
}
