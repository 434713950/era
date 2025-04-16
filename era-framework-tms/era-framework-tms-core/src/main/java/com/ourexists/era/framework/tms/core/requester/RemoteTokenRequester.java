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

package com.ourexists.era.framework.tms.core.requester;


/**
 * @author pengcheng
 * @date 2022/7/21 9:56
 * @since 1.0.0
 */
public interface RemoteTokenRequester {

    /**
     * 获取token
     *
     * @param appId     应用id
     * @param appSecret 应用密码
     * @param uri       请求地址
     * @return
     */
    String gainToken(String appId, String appSecret, String uri);

    /**
     * 请求器名称
     *
     * @return
     */
    String name();
}
