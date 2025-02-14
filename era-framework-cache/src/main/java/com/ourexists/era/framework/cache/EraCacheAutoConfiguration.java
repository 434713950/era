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

package com.ourexists.era.framework.cache;

import org.redisson.api.RedissonClient;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;

/**
 * @author pengcheng
 * @version 1.1.0
 * @date 2023/2/24 17:01
 * @since 1.1.0
 */
@EnableCaching
public class EraCacheAutoConfiguration {

    @Value("${era.cache.config:}")
    private String configLocation;

    @Bean
    public CacheManager cacheManager(RedissonClient redissonClient) {
        if (StringUtils.isEmpty(configLocation)) {
            return new RedissonSpringCacheManager(redissonClient);
        } else {
            return new RedissonSpringCacheManager(redissonClient, configLocation);
        }
    }
}
