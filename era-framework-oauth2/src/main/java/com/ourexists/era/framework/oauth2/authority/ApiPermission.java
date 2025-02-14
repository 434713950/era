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

import java.io.Serializable;

/**
 * @author pengcheng
 * @date 2022/4/15 17:10
 * @since 1.0.0
 */
public interface ApiPermission extends Serializable {

    /**
     * 获得服务名
     * @return
     */
    String getServerName();

    /**
     * 获得路径正则
     * @return
     */
    String getPathPattern();
}
