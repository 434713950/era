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

package com.ourexists.era.framework.oauth2.authority;

/**
 * @author pengcheng
 * @date 2022/4/15 17:10
 * @since 1.0.0
 */
public class DefaultApiPermission implements ApiPermission{

    private final String serverName;

    private final String pathPattern;

    public DefaultApiPermission(String serverName, String pathPattern) {
        this.serverName = serverName;
        this.pathPattern = pathPattern;
    }

    @Override
    public String getServerName() {
        return serverName;
    }

    @Override
    public String getPathPattern() {
        return pathPattern;
    }
}
