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

package com.ourexists.era.framework.scheduler.core.accesstoken.manager;

import com.ourexists.era.framework.scheduler.core.accesstoken.JobAccessToken;
import com.ourexists.era.framework.scheduler.core.accesstoken.JobProperties;
import com.ourexists.era.framework.scheduler.core.accesstoken.JobRemoteTokenRequester;
import com.ourexists.era.framework.scheduler.core.accesstoken.execption.JobException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * 小程序认证令牌管理器
 *
 * @author linzhihao
 * @date 2020/9/4 10:33
 */
@Slf4j
public class RedisJobAccessTokenManager implements JobAccessTokenManger {

    private final RedissonClient redisson;

    private final JobRemoteTokenRequester jobRemoteTokenRequester;

    private final JobProperties jobProperties;

    private static final String CACHE_KEY = "era:job:access_key";

    private static final String CACHE_LOCK = "era:job:access_lock";

    private static final Integer LOCK_TIME = 60;


    public RedisJobAccessTokenManager(RedissonClient redisson,
                                      JobProperties jobProperties,
                                      JobRemoteTokenRequester jobRemoteTokenRequester) {
        this.redisson = redisson;
        this.jobProperties = jobProperties;
        this.jobRemoteTokenRequester = jobRemoteTokenRequester;
    }

    /**
     * 刷新令牌
     *
     * @return 令牌
     */
    @Override
    public JobAccessToken accessToken() {
        RBucket<JobAccessToken> rBucket = redisson.getBucket(CACHE_KEY);
        JobAccessToken redisAccessToken = rBucket.get();
        if (redisAccessToken == null) {
            redisAccessToken = refreshLocalAccessToken(rBucket, false);
        }
        return redisAccessToken;
    }

    /**
     * 刷新令牌。如令牌未过期将使用原令牌
     *
     * @return 令牌
     */
    @Override
    public JobAccessToken refresh() {
        RBucket<JobAccessToken> rBucket = redisson.getBucket(CACHE_KEY);
        return refreshLocalAccessToken(rBucket, true);
    }

    /**
     * 强制刷新令牌。如令牌未过期也将重新申请
     *
     * @return 令牌
     */
    @Override
    public JobAccessToken forceRefresh() {
        RBucket<JobAccessToken> rBucket = redisson.getBucket(CACHE_KEY);
        return refreshLocalAccessToken(rBucket, false);
    }

    /**
     * 刷新本地令牌
     *
     * @param rBucket          redission
     * @param checkConcurrency 是否校验并发问题
     * @return 本地令牌
     */
    private JobAccessToken refreshLocalAccessToken(
            RBucket<JobAccessToken> rBucket,
            boolean checkConcurrency) {
        if (!jobProperties.getRefreshEnable()) {
            log.info("【Job认证令牌管理器】未开启accessToken刷新能力!");
            return null;
        }

        JobAccessToken jobAccessToken = null;
        RLock lock = redisson.getLock(CACHE_LOCK);
        try {
            if (lock.tryLock(0, LOCK_TIME, TimeUnit.SECONDS)) {
                //防止令牌重复刷新
                if (checkConcurrency) {
                    jobAccessToken = rBucket.get();
                    if (jobAccessToken != null
                            && System.currentTimeMillis() - jobAccessToken.getTime() < LOCK_TIME * 1000) {
                        //令牌刷新时间小于最大锁失效时间代表争抢刷新
                        log.debug("【Job认证令牌管理器】令牌无需再次刷新");
                        return jobAccessToken;
                    }
                }

                String token = jobRemoteTokenRequester.gainToken(
                        jobProperties.getAppId(),
                        jobProperties.getAppSecret(),
                        jobProperties.getUrl());

                if (StringUtils.isEmpty(token)) {
                    return null;
                }
                jobAccessToken = new JobAccessToken();
                jobAccessToken.setAccessToken(token);
                jobAccessToken.setExpiresIn(jobProperties.getTokenExpireIn());
                jobAccessToken.setTime(System.currentTimeMillis());
                //优先于官方失效
                rBucket.set(jobAccessToken, jobAccessToken.getExpiresIn(), TimeUnit.SECONDS);
            }
        } catch (JobException | InterruptedException e) {
            log.error("【Job认证令牌管理器】刷新本地令牌失败！", e);
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
}
