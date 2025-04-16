package com.ourexists.era.framework.tms.core;


import com.ourexists.era.framework.tms.core.manager.DefaultThirdAccessTokenManager;
import com.ourexists.era.framework.tms.core.manager.EraThirdAccessTokenManger;
import com.ourexists.era.framework.tms.core.requester.RemoteTokenRequester;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.List;

public class EraTmsAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public EraThirdAccessTokenManger eraThirdAccessTokenManger(List<RemoteTokenRequester> remoteTokenRequester) {
        return new DefaultThirdAccessTokenManager(remoteTokenRequester);
    }
}
