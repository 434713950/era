package com.ourexists.era.framework.tms;

import com.ourexists.era.framework.tms.core.manager.EraThirdAccessTokenManger;
import com.ourexists.era.framework.tms.core.requester.RemoteTokenRequester;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;

import java.util.List;

public class EraTmsRedisAutoConfiguration {

    @Bean
    public EraThirdAccessTokenManger eraThirdAccessTokenManger(
            RedissonClient redisson,
            List<RemoteTokenRequester> remoteTokenRequester) {
        return new RedisThirdAccessTokenManager(redisson, remoteTokenRequester);
    }
}
