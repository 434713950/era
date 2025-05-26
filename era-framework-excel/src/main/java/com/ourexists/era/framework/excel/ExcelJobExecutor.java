package com.ourexists.era.framework.excel;

import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSON;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;

import java.util.Map;

import static com.ourexists.era.framework.excel.export.ExportJobKey.JOB_BEAN_PREFIX;

public class ExcelJobExecutor {

    private JobLauncher jobLauncher;

    private Map<String, Job> jobMaps;

    public ExcelJobExecutor(JobLauncher jobLauncher,
                            Map<String, Job> jobMaps) {
        this.jobLauncher = jobLauncher;
        this.jobMaps = jobMaps;
    }

    public <T> JobExecution execute(String executorName,
                                    String exportId,
                                    T condition,
                                    String fileName,
                                    ExcelTypeEnum excelType) throws Exception {
        JobParametersBuilder builder = new JobParametersBuilder()
                .addString("exportId", exportId)
                .addString("condition", JSON.toJSONString(condition))
                .addString("fileName", fileName)
                .addString("excelType", excelType.name())
                .addLong("timestamp", System.currentTimeMillis());//保证唯一
        JobParameters jobParameters = builder.toJobParameters();
        return jobLauncher.run(jobMaps.get(JOB_BEAN_PREFIX + executorName), jobParameters);
    }
}
