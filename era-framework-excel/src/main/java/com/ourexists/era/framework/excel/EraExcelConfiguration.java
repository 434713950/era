package com.ourexists.era.framework.excel;

import com.ourexists.era.framework.excel.export.ExcelTaskerConfigurer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Map;

@Configuration
@EnableBatchProcessing
@Import(ExcelTaskerConfigurer.class)
public class EraExcelConfiguration {

    @Bean
    public ExcelJobExecutor excelJobExecutor(JobLauncher jobLauncher,
                                             Map<String, Job> jobMaps) {
        return new ExcelJobExecutor(jobLauncher, jobMaps);
    }
}
