package com.ourexists.era.framework.excel.export;

import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.fastjson2.JSON;
import com.ourexists.era.framework.excel.ExportDataHandler;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.List;
import java.util.Map;

public class ExportInitTasklet implements Tasklet {

    private ExportDataHandler exportDataHandler;

    public ExportInitTasklet(ExportDataHandler exportDataHandler) {
        this.exportDataHandler = exportDataHandler;
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        Map<String, Object> jobParametersMap = chunkContext.getStepContext().getJobParameters();
        Object condition = JSON.parseObject((String) jobParametersMap.get(ExportJobKey.CONDITION));
        String exportId = (String) jobParametersMap.get(ExportJobKey.EXPORT_ID);
        String fileName = (String) jobParametersMap.get(ExportJobKey.FILENAME);
        ExcelTypeEnum excelType = ExcelTypeEnum.valueOf((String) jobParametersMap.get(ExportJobKey.EXCEL_TYPE));
        exportDataHandler.beforeExport(exportId, condition, fileName, excelType);

        ExportPartDataResult exportPartDataResult = new ExportPartDataResult()
                .setPartNum(1)
                .setIsEnd(false)
                .setIncludeColumnIndexes(exportDataHandler.includeColumnIndexes(condition))
                .setFileName(fileName);
        List<WriteHandler> writeHandlers = exportDataHandler.loadWriteHandlers(condition);

        Map<String, Object> context = chunkContext.getStepContext().getJobExecutionContext();
        context.put("exportPartDataResult", exportPartDataResult);
        context.put("writeHandlers", writeHandlers);
        return RepeatStatus.FINISHED;
    }
}
