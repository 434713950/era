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

import com.ourexists.era.framework.scheduler.core.accesstoken.Job;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

/**
 * @author pengcheng
 * @date 2021/3/10 9:39
 * @since 1.0.0
 */
@Getter
@Setter
@Accessors(chain = true)
public class XxlJob {

    /**
     * 执行器编号,必填
     */
    private Integer jobGroup;

    /**
     * 描述，必填
     */
    private String jobDesc;

    /**
     * cron表达式
     */
    private String cronGen_display;

    /**
     * cron表达式
     */
    private String jobCron;


    /**
     * 负责人.必填
     */
    private String author;

    /**
     * 报警接受邮箱.必填
     */
    private String alarmEmail;


    /**
     * 路由执行器策略
     */
    private String executorRouteStrategy;

    /**
     * 执行器，任务Handler名称.必填
     */
    private String executorHandler;

    /**
     * 任务参数
     */
    private String executorParam;

    /**
     * 阻塞处理策略
     */
    private String executorBlockStrategy;

    /**
     * 子任务id。,分割
     */
    private String childJobId;

    /**
     * 任务执行超时时间，单位秒
     */
    private Long executorTimeout;

    /**
     * 失败重试次数
     */
    private Integer executorFailRetryCount;

    /**
     * GLUE类型
     */
    private String glueType;

    /**
     * GLUE备注
     */
    private String glueRemark;

    /**
     * 调度状态：0-停止，1-运行
     */
    private Integer triggerStatus;

    /**
     * 上次调度时间
     */
    private Long triggerLastTime;

    /**
     * 下次调度时间
     */
    private Long triggerNextTime;

    public static XxlJob warp(Job source) {
        XxlJob target= new XxlJob();
        BeanUtils.copyProperties(source, target);
        target.setCronGen_display(source.getCron());
        return target;
    }
}
