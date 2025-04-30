package com.ourexists.era.framework.tms.core.manager;

import com.ourexists.era.framework.tms.core.requester.RemoteTokenRequester;
import com.ourexists.era.framework.tms.core.requester.Connection;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class DefaultThirdAccessTokenManager extends AbstractThirdAccessTokenManager {

    private final Map<String, EraThirdAccessToken> thirdAccessTokenMap = new ConcurrentHashMap<>();

    public DefaultThirdAccessTokenManager(List<RemoteTokenRequester> remoteTokenRequester) {
        super(remoteTokenRequester);
    }

    public DefaultThirdAccessTokenManager(
            List<Connection> connections,
            List<RemoteTokenRequester> remoteTokenRequester) {
        super(remoteTokenRequester);
        connections.forEach(properties -> {
            propertiesMap.put(properties.getName(), properties);
        });
    }

    @Override
    public EraThirdAccessToken accessToken(String connectName) {
        return thirdAccessTokenMap.get(connectName);
    }

    @Override
    public EraThirdAccessToken refresh(String connectName) {
        return refreshAccessToken(connectName);
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

    @Override
    public EraThirdAccessToken forceRefresh(String connectName) {
        return refreshAccessToken(connectName);
    }

    @Override
    public void addApp(Connection connection) {
        propertiesMap.put(connection.getName(), connection);
        refreshAccessToken(connection.getName());
    }

    @Override
    public void removeApp(String connectName) {
        propertiesMap.remove(connectName);
        thirdAccessTokenMap.remove(connectName);
    }

    private EraThirdAccessToken refreshAccessToken(String connectName) {
        Connection connection = getProperties(connectName);
        if (connection == null) {
            log.error("【Token Manager】connect[{}]not exists", connectName);
            return null;
        }
        if (!connection.getRefreshEnable()) {
            log.info("【Token Manager】Access Token refresh capability not enabled!");
            return null;
        }
        String token = getRemoteTokenRequester(connection.getName()).gainToken(
                connection.getAppId(),
                connection.getAppSecret(),
                connection.getTokenUri());
        return new EraThirdAccessToken()
                .setAccessToken(token)
                .setTime(System.currentTimeMillis())
                .setExpiresIn(connection.getTokenExpireIn());
    }

    private Connection getProperties(String connectName) {
        return propertiesMap.get(connectName);
    }
}
