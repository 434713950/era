/*
 * Copyright (c) 2025. created by ourexists.https://gitee.com/ourexists
 */

package com.ourexists.era.txflow.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

@Getter
@Setter
@Accessors(chain = true)
public class Tx {

    private String id;

    /**
     * 同步分类
     */
    private String tx;

    /**
     * 分片开始时间戳
     */
    private Date partStartTimestamp;

    /**
     * 分片结束时间戳
     */
    private Date partEndTimestamp;

    /**
     * 同步状态
     */
    private String status;

    /**
     * 上一分片的最小值
     */
    private String preMin;

    /**
     * 上一分片的最大值
     */
    private String preMax;

    /**
     * 分片最小值
     */
    private String partMin;

    /**
     * 分片最大值
     */
    private String partMax;

    private Date createdTime;

    private Date updatedTime;
}
