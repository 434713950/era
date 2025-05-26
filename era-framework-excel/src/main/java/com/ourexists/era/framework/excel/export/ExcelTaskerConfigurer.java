package com.ourexists.era.framework.excel.export;

import com.ourexists.era.framework.excel.ExportDataHandler;
import com.ourexists.era.framework.oss.OssTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

import java.util.Map;

@Slf4j
public class ExcelTaskerConfigurer implements BeanDefinitionRegistryPostProcessor {

    private JobBuilderFactory jobBuilderFactory;

    private StepBuilderFactory stepBuilderFactory;

    private Map<String, ExportDataHandler> exportExecutorMaps;

    private OssTemplate ossTemplate;

    public ExcelTaskerConfigurer(JobBuilderFactory jobBuilderFactory,
                                 StepBuilderFactory stepBuilderFactory,
                                 Map<String, ExportDataHandler> exportExecutorMaps,
                                 OssTemplate ossTemplate) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.exportExecutorMaps = exportExecutorMaps;
        this.ossTemplate = ossTemplate;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        for (Map.Entry<String, ExportDataHandler> entry : exportExecutorMaps.entrySet()) {
            ExportDataHandler exportDataHandler = entry.getValue();
            Step one = stepBuilderFactory.get("era_exportInitStep")
                    .tasklet(new ExportInitTasklet(exportDataHandler))
                    .listener(new ExportInitTaskletListener(exportDataHandler))
                    .build();
            Step two = stepBuilderFactory.get("era_exportRwStep")
                    .tasklet(new ExportRwTasklet(exportDataHandler))
                    .listener(new ExportRwTaskletListener(exportDataHandler))
                    .build();
            Step three = stepBuilderFactory.get("era_exportUploadStep")
                    .tasklet(new ExportUploadOssTasklet(exportDataHandler, ossTemplate))
                    .listener(new ExportUploadOssTaskletListener(exportDataHandler))
                    .build();
            BeanDefinitionBuilder jobBeanBuilder = BeanDefinitionBuilder
                    .genericBeanDefinition(Job.class, () -> jobBuilderFactory.get(entry.getKey())
                            .start(one)
                            .next(two)
                            .next(three)
                            .build());
            registry.registerBeanDefinition(ExportJobKey.JOB_BEAN_PREFIX + entry.getKey(), jobBeanBuilder.getBeanDefinition());
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}
