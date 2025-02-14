/*
 * Copyright (C) 2025  ChengPeng
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package com.ourexists.era.framework.scheduler.xxljob.configure;

import com.ourexists.era.framework.scheduler.core.accesstoken.JobProperties;
import com.ourexists.era.framework.scheduler.core.accesstoken.JobRemoteTokenRequester;
import com.ourexists.era.framework.scheduler.core.accesstoken.manager.JobAccessTokenManger;
import com.ourexists.era.framework.scheduler.core.accesstoken.template.JobOperateTemplate;
import com.ourexists.era.framework.scheduler.xxljob.XxlJobOperateTemplate;
import com.ourexists.era.framework.scheduler.xxljob.XxlJobRemoteTokenRequester;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

/**
 * @author pengcheng
 * @date 2021/3/10 17:28
 * @since 1.1.0
 */
@EnableScheduling
public class XxlJobAutoConfiguration {

    @Bean
    public JobOperateTemplate jobOperateTemplate(RestTemplate restTemplate,
                                                 JobAccessTokenManger jobAccessTokenManger,
                                                 JobProperties jobProperties) {
        return new XxlJobOperateTemplate(restTemplate, jobAccessTokenManger, jobProperties);
    }

    @Bean
    public JobRemoteTokenRequester jobRemoteTokenRequester(RestTemplate restTemplate) {
        return new XxlJobRemoteTokenRequester(restTemplate);
    }
}
