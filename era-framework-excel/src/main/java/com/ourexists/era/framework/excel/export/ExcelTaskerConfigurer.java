package com.ourexists.era.framework.excel.export;

import com.ourexists.era.framework.excel.ExportDataHandler;
import com.ourexists.era.framework.oss.OssTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;

@Slf4j
public class ExcelTaskerConfigurer implements InitializingBean {

    private JobRepository jobRepository;

    private PlatformTransactionManager transactionManager;

    private Map<String, ExportDataHandler> exportExecutorMaps;

    private OssTemplate ossTemplate;

    private ConfigurableApplicationContext context;


    public ExcelTaskerConfigurer(JobRepository jobRepository,
                                 PlatformTransactionManager transactionManager,
                                 Map<String, ExportDataHandler> exportExecutorMaps,
                                 OssTemplate ossTemplate,
                                 ConfigurableApplicationContext context) {
        this.exportExecutorMaps = exportExecutorMaps;
        this.ossTemplate = ossTemplate;
        this.context = context;
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) context.getBeanFactory();
        for (Map.Entry<String, ExportDataHandler> entry : exportExecutorMaps.entrySet()) {
            ExportDataHandler exportDataHandler = entry.getValue();
            Step one = new StepBuilder("era_export_init_step_" + entry.getKey(), jobRepository)
                    .tasklet(new ExportInitTasklet(exportDataHandler), transactionManager)
                    .listener(new ExportInitTaskletListener(exportDataHandler))
                    .build();
            Step two = new StepBuilder("era_export_rw_step_" + entry.getKey(), jobRepository)
                    .tasklet(new ExportRwTasklet(exportDataHandler), transactionManager)
                    .listener(new ExportRwTaskletListener(exportDataHandler))
                    .build();
            Step three = new StepBuilder("era_export_upload_step_" + entry.getKey(), jobRepository)
                    .tasklet(new ExportUploadOssTasklet(exportDataHandler, ossTemplate), transactionManager)
                    .listener(new ExportUploadOssTaskletListener(exportDataHandler))
                    .build();
            BeanDefinitionBuilder jobBeanBuilder = BeanDefinitionBuilder
                    .genericBeanDefinition(Job.class, () -> new JobBuilder(entry.getKey(), jobRepository)
                            .start(one)
                            .next(two)
                            .next(three)
                            .build());
            registry.registerBeanDefinition(ExportJobKey.JOB_BEAN_PREFIX + entry.getKey(), jobBeanBuilder.getBeanDefinition());
        }
    }
}
