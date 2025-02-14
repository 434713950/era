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
 * @date 2021/5/26 10:05
 * @since 1.0.0
 */
@Setter
@Getter
public class JobListInfo {

    private Integer id;

    /**
     * 添加时间
     */
    private Long addTime;

    /**
     * 报警邮件
     */
    private String alarmEmail;

    /**
     * 管理人
     */
    private String author;

    /**
     * 子任务ID
     */
    private Long childJobId;

    /**
     * 阻塞处理策略
     */
    private String executorBlockStrategy;

    /**
     * 失败重试次数
     */
    private Integer executorFailRetryCount;

    /**
     * JobHandler
     */
    private String executorHandler;

    /**
     * 执行参数
     */
    private String executorParam;

    /**
     * 路由策略
     */
    private String executorRouteStrategy;

    /**
     * 任务超时
     */
    private Integer executorTimeout;

    /**
     * 运行备注
     */
    private String glueRemark;

    /**
     * 运行源
     */
    private String glueSource;

    /**
     * 运行模式
     */
    private String glueType;

    /**
     * 运行更新时间
     */
    private Long glueUpdatetime;


    private String jobCron;

    private String jobDesc;

    private Integer jobGroup;

    private Integer triggerLastTime;

    private Integer triggerNextTime;

    /**
     * 执行器状态
     */
    private Integer triggerStatus;

    /**
     * 执行器修改时间
     */
    private Long updateTime;
}
