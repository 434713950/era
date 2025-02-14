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

package com.ourexists.era.framework.scheduler.core.accesstoken;

import com.ourexists.era.framework.scheduler.core.accesstoken.template.JobOperateTemplate;
import com.ourexists.era.framework.scheduler.core.accesstoken.manager.JobAccessTokenManger;
import com.ourexists.era.framework.scheduler.core.accesstoken.manager.RedisJobAccessTokenManager;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * @author pengcheng
 * @version 1.1.0
 * @date 2023/2/24 15:25
 * @since 1.1.0
 */
@ConditionalOnClass({JobRemoteTokenRequester.class, JobOperateTemplate.class})
@Import({JobAccessTokenTimer.class, JobProperties.class})
public class JobAutoApplication {


    @Bean
    @ConditionalOnMissingBean
    public JobAccessTokenManger jobAccessTokenManger(RedissonClient redisson,
                                                     JobProperties jobProperties,
                                                     JobRemoteTokenRequester jobRemoteTokenRequester) {
        return new RedisJobAccessTokenManager(redisson, jobProperties, jobRemoteTokenRequester);
    }
}
