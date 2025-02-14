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

package com.ourexists.era.framework.oauth2.resource.permission.store;

import com.ourexists.era.framework.oauth2.authority.ApiPermission;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;

import java.util.Collection;

/**
 * @author pengcheng
 * @date 2022/4/15 18:00
 * @since 1.0.0
 */
public class RedisPermissionStore implements PermissionStore {

    private static final String CACHE_PREFIX = "era:oauth2:permission:";


    private static final String API_CACHE_PREFIX = CACHE_PREFIX + "verify";

    private final RedissonClient redissonClient;

    public RedisPermissionStore(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public void setPermission(Collection<ApiPermission> apiPermissions) {
        RList<ApiPermission> rList = redissonClient.getList(API_CACHE_PREFIX);
        if (rList.isExists()) {
            rList.clear();
        }
        rList.addAll(apiPermissions);
    }

    @Override
    public Collection<ApiPermission> apiPermissions() {
        RList<ApiPermission> rList = redissonClient.getList(API_CACHE_PREFIX);
        if (rList.isExists()) {
            return rList.readAll();
        }
        return null;
    }

    @Override
    public void clear() {
        RList<? extends ApiPermission> rList = redissonClient.getList(API_CACHE_PREFIX);
        if (rList.isExists()) {
            rList.delete();
        }
    }

    @Override
    public void insert(ApiPermission apiPermission) {
        RList<ApiPermission> rList = redissonClient.getList(API_CACHE_PREFIX);
        rList.add(apiPermission);
    }
}
