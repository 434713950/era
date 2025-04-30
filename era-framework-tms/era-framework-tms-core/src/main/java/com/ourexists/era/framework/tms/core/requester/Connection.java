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

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author pengcheng
 * @version 1.0.0
 * @date 2023/2/24 14:17
 * @since 1.0.0
 */
@Setter
@Getter
@Accessors(chain = true)
public class Connection {

    /**
     * 连接名
     */
    private String name;

    /**
     * token请求路径
     */
    private String tokenUri;

    /**
     * 应用id
     */
    private String appId;

    /**
     * 应用密码
     */
    private String appSecret;


    /**
     * token过期时间
     */
    private Long tokenExpireIn;

    /**
     * 是否可定时刷新
     */
    private Boolean refreshEnable = true;

    /**
     * 请求重试次数
     */
    private Integer retry = 3;
}
