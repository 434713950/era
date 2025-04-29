/*
 * Copyright (c) 2025. created by ourexists.https://gitee.com/ourexists
 */

package com.ourexists.era.txflow;

import com.ourexists.era.txflow.model.Tx;
import com.ourexists.era.txflow.model.TxBranch;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class TxTransfer {

    /**
     * 当前事务主体
     */
    private Tx tx;

    /**
     * 当前的
     */
    private TxBranch branch;

    /**
     * 当前同步流转的数据
     */
    private String jsonData;


}
