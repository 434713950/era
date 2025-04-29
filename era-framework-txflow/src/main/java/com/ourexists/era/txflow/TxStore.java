package com.ourexists.era.txflow;


import com.ourexists.era.txflow.model.Tx;
import com.ourexists.era.txflow.model.TxBranch;

import java.util.Date;

public interface TxStore {

    /**
     * 同一事务最后的流程点
     *
     * @param txName 事务组名
     * @return
     */
    Tx lastTx(String txName);

    /**
     * 事务启动处理，开始时推荐将数据存储
     *
     * @param tx
     */
    void txStart(Tx tx);

    /**
     * 事务错误处理
     *
     * @param tx
     */
    void txError(Tx tx);

    /**
     * 修改事务状态
     *
     * @param tx   事务
     */
    void txEnd(Tx tx);

    /**
     * 事务中最后的执行分支
     *
     * @param txId 事务id
     * @return
     */
    TxBranch lastBranch(String txId);

    /**
     * 事务分支开始,开始时推荐将数据存储
     * @param txBranch
     */
    void branchStart(TxBranch txBranch);

    /**
     * 事务分支错误处理
     * @param txBranch
     */
    void branchError(TxBranch txBranch);

    /**
     * 事务分支结束
     * @param txBranch
     */
    void branchEnd(TxBranch txBranch);

    /**
     * 清除事务
     * @param txName    事务名
     */
    void clearTx(String txName);

    /**
     * 清除所有事务
     */
    void clearAll();

    /**
     * 清除时间前的历史
     *
     * @param date
     */
    void clearHistory(Date date);
}
