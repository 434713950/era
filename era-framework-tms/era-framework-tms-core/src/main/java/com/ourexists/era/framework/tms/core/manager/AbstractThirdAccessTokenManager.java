package com.ourexists.era.framework.tms.core.manager;

import com.ourexists.era.framework.tms.core.requester.Connection;
import com.ourexists.era.framework.tms.core.requester.RemoteTokenRequester;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractThirdAccessTokenManager implements EraThirdAccessTokenManger {

    private final List<RemoteTokenRequester> remoteTokenRequester = new ArrayList<>();

    protected final Map<String, Connection> propertiesMap = new ConcurrentHashMap<>();

    protected AbstractThirdAccessTokenManager(List<RemoteTokenRequester> remoteTokenRequester) {
        this.remoteTokenRequester.addAll(remoteTokenRequester);
    }

    protected RemoteTokenRequester getRemoteTokenRequester(String connectName) {
        for (RemoteTokenRequester tokenRequester : remoteTokenRequester) {
            if (tokenRequester.connectName().equals(connectName)) {
                return tokenRequester;
            }
        }
        return null;
    }
}
