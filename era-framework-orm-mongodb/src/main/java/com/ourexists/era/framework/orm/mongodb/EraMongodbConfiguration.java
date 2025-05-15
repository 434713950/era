/*
 * Copyright (c) 2025. created by ourexists.https://gitee.com/ourexists
 */

package com.ourexists.era.framework.orm.mongodb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.convert.MongoConverter;

/**
 * @author pengcheng
 * @date 2022/7/29 13:41
 * @since 1.0.0
 */
@Configuration
public class EraMongodbConfiguration {

    @Bean
    EraMongoTemplate mongoTemplate(MongoDatabaseFactory factory, MongoConverter converter) {
        return new EraMongoTemplate(factory, converter);
    }
}
