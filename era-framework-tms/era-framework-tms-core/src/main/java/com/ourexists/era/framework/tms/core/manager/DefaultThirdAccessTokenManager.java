package com.ourexists.era.framework.tms.core.manager;

import com.ourexists.era.framework.tms.core.requester.RemoteTokenRequester;
import com.ourexists.era.framework.tms.core.requester.Connection;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class DefaultThirdAccessTokenManager extends AbstractThirdAccessTokenManager {

    private final Map<String, Connection> propertiesMap = new ConcurrentHashMap<>();

    private final Map<String, EraThirdAccessToken> thirdAccessTokenMap = new ConcurrentHashMap<>();

    public DefaultThirdAccessTokenManager(List<RemoteTokenRequester> remoteTokenRequester) {
        super(remoteTokenRequester);
    }

    public DefaultThirdAccessTokenManager(
            List<Connection> connections,
            List<RemoteTokenRequester> remoteTokenRequester) {
        super(remoteTokenRequester);
        connections.forEach(properties -> {
            propertiesMap.put(properties.getAppId(), properties);
        });
    }

    @Override
    public EraThirdAccessToken accessToken(String appId) {
        return thirdAccessTokenMap.get(appId);
    }

    @Override
    public EraThirdAccessToken refresh(String appId) {
        return refreshAccessToken(appId);
    }

    @Override
    public EraThirdAccessToken forceRefresh(String appId) {
        return refreshAccessToken(appId);
    }

    @Override
    public void addApp(Connection properties) {
        propertiesMap.put(properties.getAppId(), properties);
        refreshAccessToken(properties.getAppId());
    }

    @Override
    public void removeApp(String appId) {
        propertiesMap.remove(appId);
        thirdAccessTokenMap.remove(appId);
    }

    private EraThirdAccessToken refreshAccessToken(String appId) {
        Connection connection = getProperties(appId);
        if (connection == null) {
            log.error("【Token Manager】appId[{}]not exists", appId);
            return null;
        }
        if (!connection.getRefreshEnable()) {
            log.info("【Token Manager】Access Token refresh capability not enabled!");
            return null;
        }
        String token = getRemoteTokenRequester(connection.getRequester()).gainToken(
                connection.getAppId(),
                connection.getAppSecret(),
                connection.getTokenUri());
        return new EraThirdAccessToken()
                .setAccessToken(token)
                .setTime(System.currentTimeMillis())
                .setExpiresIn(connection.getTokenExpireIn());
    }

    private Connection getProperties(String appId) {
        return propertiesMap.get(appId);
    }
}
