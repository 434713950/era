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

package com.ourexists.era.framework.scheduler.xxljob.support;

import lombok.Getter;
import lombok.Setter;

/**
 * @author pengcheng
 * @date 2021/5/26 10:17
 * @since 1.0.0
 */
@Getter
@Setter
public class JobGroupPageReq {

    private Integer jobGroup;

    /**
     * 执行器状态。-1:全部（默认）
     */
    private Integer triggerStatus = -1;

    /**
     * 任务描述
     */
    private String jobDesc;

    /**
     * 执行器处理器
     */
    private String executorHandler;

    /**
     * 处理人
     */
    private String author;

    /**
     * 从0开始，对标数据库limit查询
     */
    private Integer start;

    /**
     * 对标数据库limit查询
     */
    private Integer length;
}
