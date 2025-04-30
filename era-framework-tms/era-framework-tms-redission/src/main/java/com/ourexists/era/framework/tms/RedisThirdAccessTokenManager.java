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

package com.ourexists.era.framework.tms;

import com.ourexists.era.framework.tms.core.manager.AbstractThirdAccessTokenManager;
import com.ourexists.era.framework.tms.core.manager.EraThirdAccessToken;
import com.ourexists.era.framework.tms.core.requester.Connection;
import com.ourexists.era.framework.tms.core.requester.RemoteTokenRequester;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author pengcheng
 * @version 1.1.0
 * @date 2023/2/24 14:34
 * @since 1.1.0
 */
@Slf4j
public class RedisThirdAccessTokenManager extends AbstractThirdAccessTokenManager {

    private final RedissonClient redisson;

    private static final String CACHE_KEY = "era:tms:access_key:";

    private static final String CACHE_LOCK = "era:tms:access_lock:";

    private static final Integer LOCK_TIME = 60;

    public RedisThirdAccessTokenManager(RedissonClient redisson, List<RemoteTokenRequester> remoteTokenRequester) {
        super(remoteTokenRequester);
        this.redisson = redisson;
    }


    public RedisThirdAccessTokenManager(RedissonClient redisson, List<Connection> connections, List<RemoteTokenRequester> remoteTokenRequester) {
        super(remoteTokenRequester);
        this.redisson = redisson;
        connections.forEach(properties -> {
            propertiesMap.put(properties.getName(), properties);
        });
    }

    /**
     * 刷新令牌
     *
     * @return 令牌
     */
    @Override
    public EraThirdAccessToken accessToken(String connectName) {
        RBucket<EraThirdAccessToken> rBucket = redisson.getBucket(CACHE_KEY + connectName);
        EraThirdAccessToken redisAccessToken = rBucket.get();
        if (redisAccessToken == null) {
            redisAccessToken = refreshAccessToken(connectName, rBucket, false);
        }
        return redisAccessToken;
    }

    /**
     * 刷新令牌。如令牌未过期将使用原令牌
     *
     * @return 令牌
     */
    @Override
    public EraThirdAccessToken refresh(String connectName) {
        RBucket<EraThirdAccessToken> rBucket = redisson.getBucket(CACHE_KEY + connectName);
        return refreshAccessToken(connectName, rBucket, true);
    }

    @Override
    public void refreshAll() {
        for (String key : this.propertiesMap.keySet()) {
            refresh(key);
        }
    }

    @Override
    public void forceRefreshAll() {
        for (String key : this.propertiesMap.keySet()) {
            forceRefresh(key);
        }
    }

    /**
     * 强制刷新令牌。如令牌未过期也将重新申请
     *
     * @return 令牌
     */
    @Override
    public EraThirdAccessToken forceRefresh(String connectName) {
        RBucket<EraThirdAccessToken> rBucket = redisson.getBucket(CACHE_KEY + connectName);
        return refreshAccessToken(connectName, rBucket, false);
    }

    @Override
    public void addApp(Connection properties) {
        propertiesMap.put(properties.getName(), properties);
        refresh(properties.getName());
    }

    @Override
    public void removeApp(String connectName) {
        propertiesMap.remove(connectName);
        redisson.getBucket(CACHE_KEY + connectName).deleteAsync();
    }

    /**
     * 刷新令牌
     *
     * @param appId            appId
     * @param rBucket          redission
     * @param checkConcurrency 是否校验并发问题
     * @return 令牌
     */
    private EraThirdAccessToken refreshAccessToken(String appId, RBucket<EraThirdAccessToken> rBucket, boolean checkConcurrency) {
        Connection connection = getProperties(appId);
        if (!connection.getRefreshEnable()) {
            log.info("【Token Manager】Access Token refresh capability not enabled!");
            return null;
        }

        EraThirdAccessToken jobAccessToken = null;
        RLock lock = redisson.getLock(CACHE_LOCK);
        try {
            if (lock.tryLock(0, LOCK_TIME, TimeUnit.SECONDS)) {
                //防止令牌重复刷新
                if (checkConcurrency) {
                    jobAccessToken = rBucket.get();
                    if (jobAccessToken != null && System.currentTimeMillis() - jobAccessToken.getTime() < LOCK_TIME * 1000) {
                        //令牌刷新时间小于最大锁失效时间代表争抢刷新
                        log.debug("【Token Manager】Token does not need to be refreshed again");
                        return jobAccessToken;
                    }
                }
                RemoteTokenRequester remoteTokenRequester = getRemoteTokenRequester(connection.getName());
                if (remoteTokenRequester == null) {
                    log.debug("【Token Manager】Connect lock of requester");
                    return null;
                }
                String token = remoteTokenRequester.gainToken(connection.getAppId(), connection.getAppSecret(), connection.getTokenUri());

                if (StringUtils.isEmpty(token)) {
                    return null;
                }
                jobAccessToken = new EraThirdAccessToken();
                jobAccessToken.setAccessToken(token);
                jobAccessToken.setExpiresIn(connection.getTokenExpireIn());
                jobAccessToken.setTime(System.currentTimeMillis());
                //优先于官方失效
                rBucket.set(jobAccessToken, jobAccessToken.getExpiresIn(), TimeUnit.SECONDS);
            }
        } catch (InterruptedException e) {
            log.error("【Token Manager】Failed to refresh local token!", e);
        } finally {
            //释放锁
            try {
                lock.unlock();
            } catch (Exception e) {
                //nothing
            }
        }
        return jobAccessToken;
    }

    private Connection getProperties(String connectName) {
        return propertiesMap.get(connectName);
    }
}
