/*
 * Copyright (c) 2025. created by ourexists.https://gitee.com/ourexists
 */

package com.ourexists.era.txflow;

import com.ourexists.era.framework.core.utils.CollectionUtil;
import com.ourexists.era.txflow.model.Tx;
import com.ourexists.era.txflow.model.TxBranch;
import com.ourexists.era.txflow.model.TxStatusEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public abstract class TxManager {

    private final List<TxBranchFlow> txBranchFlows = new ArrayList<>();

    protected TxStore txStore;

    public TxManager(TxStore txStore) {
        this.txStore = txStore;
        txBranchFlows.addAll(flows());
        if (CollectionUtil.isBlank(txBranchFlows)) {
            throw new RuntimeException("flows is empty");
        }
        txBranchFlows.sort(Comparator.comparingInt(TxBranchFlow::sort));
    }

    public void execute(String initJsonData) {
        execute(new Date(), new Date(), initJsonData);
    }

    public void execute(Date startTime, Date endTime) {
        execute(startTime, endTime, null);
    }

    public void execute(Date startTime, Date endTime, String initJsonData) {
        Tx last = txStore.lastTx(txName());
        Tx tx = new Tx()
                .setTx(txName())
                .setPartStartTimestamp(startTime)
                .setPartEndTimestamp(endTime)
                .setStatus(TxStatusEnum.start.name());
        TxTransfer txTransfer = new TxTransfer();
        txTransfer.setJsonData(initJsonData);
        if (last != null) {
            tx.setPreMax(last.getPartMax());
            tx.setPreMin(last.getPartMin());

            //基于上一个分片容错2分钟执行当前分片
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(last.getPartEndTimestamp());
            calendar.add(Calendar.MINUTE, -2);
            tx.setPartStartTimestamp(calendar.getTime());
        }
        txStore.txStart(tx);
        txTransfer.setTx(tx);
        flowBreakExecute(txTransfer);
    }

    protected void flowBreakExecute(TxTransfer txTransfer) {
        flowBreakExecute(txTransfer, null, 0);
    }

    protected void flowBreakExecute(TxTransfer txTransfer, String point) {
        flowBreakExecute(txTransfer, point, 0);
    }

    protected void flowBreakExecute(TxTransfer txTransfer, String point, int offset) {

        if (CollectionUtil.isNotBlank(txBranchFlows)) {
            int pos = -1;
            if (point == null) {
                pos = offset;
            }
            try {
                for (int i = 0; i < txBranchFlows.size(); i++) {
                    if (pos < 0 && txBranchFlows.get(i).point().equals(point)) {
                        pos = i + offset;
                    }
                }
                //偏移位置大于总流程说明完成了
                if (pos < txBranchFlows.size()) {
                    for (int i = 0; i < txBranchFlows.size(); i++) {
                        if (i >= pos) {
                            txBranchFlows.get(i).exec(txTransfer);
                        }
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                txTransfer.getTx().setStatus(TxStatusEnum.error.name());
                txStore.txError(txTransfer.getTx());
                return;
            }
        }
        txTransfer
                .getTx()
                .setStatus(TxStatusEnum.end.name());
        txStore.txEnd(txTransfer.getTx());
    }

    public abstract String txName();

    protected abstract List<TxBranchFlow> flows();

    /**
     * 流程断点处理
     */
    public void breakpointProcess(Tx tx) {
        //已经完成的说明已经异常处理完了
        if (tx.getStatus().equals(TxStatusEnum.end.name())) {
            return;
        }

        TxBranch txBranch = txStore.lastBranch(tx.getId());
        //说明流程刚开始，不关注
        if (txBranch == null) {
            return;
        }
        TxStatusEnum flowStatus = TxStatusEnum.valueOf(txBranch.getStatus());
        switch (flowStatus) {
            case start:
                //正常进行中
                return;
            case error:
                TxTransfer txTransfer = new TxTransfer()
                        .setTx(tx)
                        .setJsonData(txBranch.getReqData());
                //产生异常了,从异常处开始执行
                flowBreakExecute(txTransfer, txBranch.getPoint());
                break;
            case end:
                TxTransfer txTransfer2 = new TxTransfer()
                        .setTx(tx)
                        .setJsonData(txBranch.getRespData());
                //同步状态进行中而最后的流程为完成态,携带结束参数从下一流程点开始执行
                flowBreakExecute(txTransfer2, txBranch.getPoint(), 1);
                break;
        }
    }
}
