package com.ourexists.era.framework.excel.export;

import com.ourexists.era.framework.excel.ExportDataHandler;
import com.ourexists.era.framework.oss.OssTemplate;
import com.ourexists.era.framework.oss.exception.UploadException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Slf4j
public class ExportUploadOssTasklet implements Tasklet {

    private ExportDataHandler exportDataHandler;

    private OssTemplate ossTemplate;

    public ExportUploadOssTasklet(
            ExportDataHandler exportDataHandler,
            OssTemplate ossTemplate) {
        this.exportDataHandler = exportDataHandler;
        this.ossTemplate = ossTemplate;
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        ExecutionContext context = chunkContext.getStepContext().getStepExecution()
                .getJobExecution()
                .getExecutionContext();
        String tempFile = context.getString(ExportJobKey.RUNNING_FILE);
        File file = new File(tempFile);
        if (file.exists()) {
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(file);
                String uploadFilePath = exportDataHandler.filePath();
                if (!exportDataHandler.filePath().endsWith("/")) {
                    uploadFilePath += "/";
                }
                String finalFilePath = uploadFilePath + file.getName();
                ossTemplate.upload(fileInputStream, finalFilePath);
                context.put(ExportJobKey.RUNNING_FINAL_FILE_NAME, finalFilePath);
            } catch (IOException e) {
                log.error("临时文件异常！", e);
            } catch (UploadException e) {
                log.error("文件上传失败！", e);
            } finally {
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException ignored) {
                    }
                }
                file.delete();
            }
        }
        return RepeatStatus.FINISHED;
    }
}
