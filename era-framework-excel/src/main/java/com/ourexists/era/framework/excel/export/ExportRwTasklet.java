package com.ourexists.era.framework.excel.export;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.fastjson.JSON;
import com.ourexists.era.framework.core.exceptions.EraCommonException;
import com.ourexists.era.framework.core.utils.CollectionUtil;
import com.ourexists.era.framework.excel.ExportDataHandler;
import com.ourexists.era.framework.excel.style.ExcelTitleStrategy;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.File;
import java.util.List;
import java.util.Map;

public class ExportRwTasklet implements Tasklet {

    private ExportDataHandler exportDataHandler;

    public ExportRwTasklet(ExportDataHandler exportDataHandler) {
        this.exportDataHandler = exportDataHandler;
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        Map<String, Object> jobParametersMap = chunkContext.getStepContext().getJobParameters();
        Object condition = JSON.parseObject((String) jobParametersMap.get(ExportJobKey.CONDITION));
        ExcelTypeEnum excelTypeEnum = ExcelTypeEnum.valueOf((String) jobParametersMap.get(ExportJobKey.EXCEL_TYPE));
        Map<String, Object> context = chunkContext.getStepContext().getJobExecutionContext();
        ExportPartDataResult exportPartDataResult = (ExportPartDataResult) context.get(ExportJobKey.RUNNING_DATA);
        List<WriteHandler> writeHandlers = (List<WriteHandler>) context.get(ExportJobKey.RUNNING_STYLE);

        File file = partExportData(condition, exportDataHandler.getClass(), excelTypeEnum, exportPartDataResult, writeHandlers);

        exportPartDataResult.setPartNum(exportPartDataResult.getPartNum() + 1);
        context.put(ExportJobKey.RUNNING_FILE, file.getAbsolutePath());
        if (exportPartDataResult.getIsEnd()) {
            return RepeatStatus.FINISHED;
        } else {
            return RepeatStatus.CONTINUABLE;
        }
    }

    public <T, R> File partExportData(T condition, Class<R> head, ExcelTypeEnum excelType, ExportPartDataResult exportPartDataResult, List<WriteHandler> writeHandlers) throws EraCommonException {
        List<R> ossList = exportDataHandler.exportData(condition, exportPartDataResult);
        String[] fileNames = exportPartDataResult.getFileName().split("/");
        String sourceFileName = fileNames[fileNames.length - 1] + "-" + (exportPartDataResult.getPartNum() - 1);
        String targetFileName = fileNames[fileNames.length - 1] + "-" + exportPartDataResult.getPartNum();
        ExcelWriterBuilder excelWriterBuilder;
        if (exportPartDataResult.getPartNum() < 2) {
            //导出文件输出流
            excelWriterBuilder = EasyExcel
                    .write(targetFileName, head)
                    .relativeHeadRowIndex(1)
                    .includeColumnIndexes(exportPartDataResult.getIncludeColumnIndexes())
                    .orderByIncludeColumn(true)
                    .excelType(excelType);
            if (!excelType.equals(ExcelTypeEnum.CSV) && CollectionUtil.isNotBlank(writeHandlers)) {
                for (WriteHandler writeHandler : writeHandlers) {
                    excelWriterBuilder.registerWriteHandler(writeHandler);
                }
            }
        } else {
            excelWriterBuilder = EasyExcel
                    .write()
                    .includeColumnIndexes(exportPartDataResult.getIncludeColumnIndexes())
                    .orderByIncludeColumn(true)
                    .withTemplate(sourceFileName).file(targetFileName).needHead(false);
            if (!excelType.equals(ExcelTypeEnum.CSV) && CollectionUtil.isNotBlank(writeHandlers)) {
                for (WriteHandler writeHandler : writeHandlers) {
                    if (writeHandler instanceof ExcelTitleStrategy) {
                        continue;
                    }
                    excelWriterBuilder.registerWriteHandler(writeHandler);
                }
            }
        }

        excelWriterBuilder.sheet().doWrite(ossList);

        File sourceFile = new File(sourceFileName);
        File targetFile = new File(targetFileName);
        if (sourceFile.exists()) {
            sourceFile.delete();
        }
        return targetFile;
    }
}
