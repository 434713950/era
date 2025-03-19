package com.ourexists.era.framework.oauth2.resource.permission.store;

import com.ourexists.era.framework.oauth2.authority.ApiPermission;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryPermissionStore implements PermissionStore {

    private final ConcurrentHashMap<String, Collection<ApiPermission>> cache = new ConcurrentHashMap<>();

    private static final String CACHE_PREFIX = "era:oauth2:permission:";


    private static final String API_CACHE_PREFIX = CACHE_PREFIX + "verify";


    @Override
    public void setPermission(Collection<ApiPermission> apiPermissions) {
        cache.put(API_CACHE_PREFIX, apiPermissions);
    }

    @Override
    public Collection<ApiPermission> apiPermissions() {
        return cache.get(API_CACHE_PREFIX);
    }

    @Override
    public void clear() {
        cache.remove(API_CACHE_PREFIX);
    }

    @Override
    public void insert(ApiPermission apiPermission) {
        Collection<ApiPermission> apiPermissions = cache.get(API_CACHE_PREFIX);
        if (apiPermissions == null) {
            apiPermissions = Collections.emptyList();
        }
        apiPermissions.add(apiPermission);
    }
}
