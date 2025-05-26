package com.ourexists.era.framework.excel.export;

import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSON;
import com.ourexists.era.framework.excel.ExportDataHandler;
import org.springframework.batch.core.*;

import java.util.List;

public class ExportInitTaskletListener implements StepExecutionListener {

    private ExportDataHandler exportDataHandler;

    public ExportInitTaskletListener(ExportDataHandler exportDataHandler) {
        this.exportDataHandler = exportDataHandler;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        JobParameters jobParameters = stepExecution.getJobParameters();
        Object condition = JSON.parseObject(jobParameters.getString(ExportJobKey.CONDITION));
        String exportId = jobParameters.getString(ExportJobKey.EXPORT_ID);
        String fileName = jobParameters.getString(ExportJobKey.FILENAME);
        ExcelTypeEnum excelType = ExcelTypeEnum.valueOf(jobParameters.getString(ExportJobKey.EXCEL_TYPE));
        exportDataHandler.beforeExport(exportId, condition, fileName, excelType);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        if (stepExecution.getStatus() == BatchStatus.COMPLETED) {
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
