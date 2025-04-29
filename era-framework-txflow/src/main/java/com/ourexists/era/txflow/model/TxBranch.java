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
public class TxBranch {

    private String id;

    /**
     * 事务id
     */
    private String txId;

    /**
     * 同步点位
     */
    private String point;

    /**
     * 点位状态
     */
    private String status;

    /**
     * 当前入参数据
     */
    private String reqData;

    /**
     * 当前出参数据
     */
    private String respData;

    /**
     * 异常信息
     */
    private String excep;


    private Date createdTime;

    private Date updatedTime;
}
