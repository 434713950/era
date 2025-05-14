/*
 * Copyright (c) 2025. created by ourexists.https://gitee.com/ourexists
 */

package com.ourexists.era.txflow;

import com.ourexists.era.framework.core.utils.id.IdWorker;
import com.ourexists.era.txflow.model.TxBranch;
import com.ourexists.era.txflow.model.TxStatusEnum;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractTxBranchFlow implements TxBranchFlow {

    private final TxStore txStore;

    public AbstractTxBranchFlow(TxStore txStore) {
        this.txStore = txStore;
    }

    @Override
    public void exec(TxTransfer txTransfer) {
        String point = point();
        TxBranch txBranch = new TxBranch()
                .setId(IdWorker.getIdStr())
                .setTxId(txTransfer.getTx().getId())
                .setPoint(point)
                .setStatus(TxStatusEnum.start.name())
                .setReqData(txTransfer.getJsonData());
        txTransfer.setBranch(txBranch);
        txStore.branchStart(txBranch);
        try {
            doExec(txTransfer);
        } catch (Exception e) {
            log.error("", e);
            txBranch.setExcep(e.getMessage())
                    .setStatus(TxStatusEnum.error.name());
            txStore.branchError(txBranch);
            throw e;
        }
        txBranch.setRespData(txTransfer.getJsonData())
                .setStatus(TxStatusEnum.end.name());
        txStore.branchEnd(txBranch);
    }

    protected abstract void doExec(TxTransfer txTransfer);

}
