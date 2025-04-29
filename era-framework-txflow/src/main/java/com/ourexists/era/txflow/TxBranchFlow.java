/*
 * Copyright (c) 2025. created by ourexists.https://gitee.com/ourexists
 */

package com.ourexists.era.txflow;

public interface TxBranchFlow {

    /**
     * 当前流程点位
     *
     * @return
     */
    String point();

    /**
     * 同步顺序
     *
     * @return
     */
    int sort();

    /**
     * 同步数据
     *
     * @param txTransfer 流转数据
     * @return
     */
    void exec(TxTransfer txTransfer);
}
