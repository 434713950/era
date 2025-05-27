package com.ourexists.era.framework.excel.export;

import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.fastjson2.JSON;
import com.ourexists.era.framework.excel.ExportDataHandler;
import org.springframework.batch.core.*;

import java.util.List;

public class ExportRwTaskletListener implements StepExecutionListener {

    private ExportDataHandler exportDataHandler;

    public ExportRwTaskletListener(ExportDataHandler exportDataHandler) {
        this.exportDataHandler = exportDataHandler;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        JobParameters jobParameters = stepExecution.getJobParameters();
        String exportId = jobParameters.getString(ExportJobKey.EXPORT_ID);
        ExcelTypeEnum excelType = ExcelTypeEnum.valueOf(jobParameters.getString(ExportJobKey.EXCEL_TYPE));
        List<WriteHandler> writeHandlers = (List<WriteHandler>) stepExecution.getExecutionContext().get(ExportJobKey.RUNNING_STYLE);
        ExportPartDataResult exportPartDataResult = (ExportPartDataResult) stepExecution.getExecutionContext().get(ExportJobKey.RUNNING_DATA);
        exportDataHandler.beforePartHandle(exportId, exportDataHandler.getClass(), excelType, exportPartDataResult, writeHandlers);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        JobParameters jobParameters = stepExecution.getJobParameters();
        Object condition = JSON.parseObject(jobParameters.getString(ExportJobKey.CONDITION));
        String exportId = jobParameters.getString(ExportJobKey.EXPORT_ID);
        String fileName = jobParameters.getString(ExportJobKey.FILENAME);
        ExcelTypeEnum excelType = ExcelTypeEnum.valueOf(jobParameters.getString(ExportJobKey.EXCEL_TYPE));

        BatchStatus status = stepExecution.getStatus();
        if (status == BatchStatus.COMPLETED) {
            List<WriteHandler> writeHandlers = (List<WriteHandler>) stepExecution.getExecutionContext().get(ExportJobKey.RUNNING_STYLE);
            ExportPartDataResult exportPartDataResult = (ExportPartDataResult) stepExecution.getExecutionContext().get(ExportJobKey.RUNNING_DATA);
            exportDataHandler.afterPartHandle(condition, exportDataHandler.exportDataClass(), excelType, exportPartDataResult, writeHandlers);
            return ExitStatus.COMPLETED;
        } else {
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
