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

package com.ourexists.era.framework.scheduler.core.accesstoken.manager;

import com.ourexists.era.framework.scheduler.core.accesstoken.JobAccessToken;

/**
 * @author pengcheng
 * @version 1.1.0
 * @date 2023/2/24 14:34
 * @since 1.1.0
 */
public interface JobAccessTokenManger {

    /**
     * 获取
     * @return accesstoken
     */
    JobAccessToken accessToken();

    /**
     * 刷新。与已存token进行判断
     *
     * @return accesstoken
     */
    JobAccessToken refresh();

    /**
     * 强制刷新。无视已存的token
     * @return
     */
    JobAccessToken forceRefresh();
}
