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

package com.ourexists.era.oauth2.core;

/**
 * @author pengcheng
 * @date 2022/4/13 18:00
 * @since 1.0.0
 */
public enum OAuth2Role {
    /**
     * 普通角色
     */
    COMMON,

    /**
     * 管理员
     */
    ADMIN;

    public static OAuth2Role valueOfName(String name) {
        for (OAuth2Role value : OAuth2Role.values()) {
            if (value.name().equals(name)) {
                return value;
            }
        }
        return OAuth2Role.COMMON;
    }
}
