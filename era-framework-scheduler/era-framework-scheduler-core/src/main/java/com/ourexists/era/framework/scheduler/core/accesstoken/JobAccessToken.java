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

package com.ourexists.era.framework.scheduler.core.accesstoken;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * redis存储token实体
 *
 * @author linzhihao
 * @date 2020/9/7 10:49
 */
@Getter
@Setter
public class JobAccessToken implements Serializable {

    private static final long serialVersionUID = 8885306071204580953L;
    /**
     * 令牌
     */
    private String accessToken;

    /**
     * 刷新token毫秒时间
     */
    private Long time;

    /**
     * 有效时间
     */
    private Long expiresIn;
}
