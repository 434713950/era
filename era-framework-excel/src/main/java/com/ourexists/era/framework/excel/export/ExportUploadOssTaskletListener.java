package com.ourexists.era.framework.excel.export;

import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson2.JSON;
import com.ourexists.era.framework.excel.ExportDataHandler;
import org.springframework.batch.core.*;

import java.util.List;

public class ExportUploadOssTaskletListener implements StepExecutionListener {

    private ExportDataHandler exportDataHandler;

    public ExportUploadOssTaskletListener(ExportDataHandler exportDataHandler) {
        this.exportDataHandler = exportDataHandler;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        BatchStatus status = stepExecution.getStatus();
        if (status == BatchStatus.COMPLETED) {
            return ExitStatus.COMPLETED;
        } else {
            JobParameters jobParameters = stepExecution.getJobParameters();
            Object condition = JSON.parseObject(jobParameters.getString(ExportJobKey.CONDITION));
            String exportId = jobParameters.getString(ExportJobKey.EXPORT_ID);
            String fileName = jobParameters.getString(ExportJobKey.FILENAME);
            ExcelTypeEnum excelType = ExcelTypeEnum.valueOf(jobParameters.getString(ExportJobKey.EXCEL_TYPE));
            List<Throwable> exceptions = stepExecution.getFailureExceptions();
            Throwable throwable = null;
            if (!exceptions.isEmpty()) {
                throwable = exceptions.get(0);
            }
            exportDataHandler.failedHandle(exportId, condition, fileName, excelType, throwable);
            return ExitStatus.FAILED;
        }
    }
}
