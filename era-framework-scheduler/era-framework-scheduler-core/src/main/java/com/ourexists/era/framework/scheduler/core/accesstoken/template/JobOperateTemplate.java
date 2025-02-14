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

package com.ourexists.era.framework.scheduler.core.accesstoken.template;

import com.ourexists.era.framework.scheduler.core.accesstoken.Job;
import com.ourexists.era.framework.scheduler.core.accesstoken.execption.JobException;

/**
 * @author pengcheng
 * @date 2021/3/9 17:03
 * @since 1.1.0
 */
public interface JobOperateTemplate {

    /**
     * 新增/编辑任务
     *
     * @param job 任务内容
     * @return 任务id
     */
    Integer addJob(Job job) throws JobException;


    /**
     * 删除任务
     *
     * @param taskId 任务id
     * @return 执行是否成功
     */
    boolean deleteJob(String taskId) throws JobException;

    /**
     * 开始任务
     *
     * @param taskId 任务id
     * @return 执行是否成功
     */
    boolean startJob(int taskId) throws JobException;

    /**
     * 停止任务
     *
     * @param taskId 任务id
     * @return 执行是否成功
     */
    boolean stopJob(int taskId) throws JobException;
}
