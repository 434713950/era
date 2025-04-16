package com.ourexists.era.framework.tms;

import com.ourexists.era.framework.tms.core.manager.EraThirdAccessTokenManger;
import com.ourexists.era.framework.tms.core.requester.RemoteTokenRequester;
import org.redisson.Redisson;
import org.springframework.context.annotation.Bean;

import java.util.List;

public class EraTmsRedisAutoConfiguration {

    @Bean
    public EraThirdAccessTokenManger eraThirdAccessTokenManger(
            Redisson redisson,
            List<RemoteTokenRequester> remoteTokenRequester) {
        return new RedisThirdAccessTokenManager(redisson, remoteTokenRequester);
    }
}
