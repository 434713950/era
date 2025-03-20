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

package com.ourexists.era.framework.oauth2;

import java.util.Arrays;
import java.util.List;

/**
 * @author pengcheng
 * @date 2022/4/8 14:03
 * @since 1.0.0
 */
public interface AuthConstants {

    /**
     * 统一认证api的前缀
     */
    String AUTHENTICATION_API_PREFIX = "/authentication";

    String OPEN_PREFIX = "/open";


    String WHITE_PREFIX = "/white";

    /**
     * 全局数据接口前缀
     */
    String OVERALL_PREFIX = "/overall";

    String OVERALL_PATH = OVERALL_PREFIX + "/**";

    /**
     * 限定数据接口前缀。即主租户的数据将会限制自身
     */
    String LIMIT_PREFIX = "/limit";

    String LIMIT_PATH = LIMIT_PREFIX + "/**";

    /**
     * 许可信息
     */
    String LICENSE = "made by ourexists";

    List<String> HEADER_WHITE_PATH = Arrays.asList("/swagger-resources/**", "/swagger-ui/**",
            OPEN_PREFIX + "/**",
            "/webjars/**", "/v3/**", "/oauth/**", "/refresh", "/error", "/health", "/static/**");

    /**
     * 系统相关的白名单path
     */
    List<String> SYSTEM_WHITE_PATH = Arrays.asList("/swagger-resources/**", "/swagger-ui/**",
            "/webjars/**", "/v3/**", "/oauth/**", "/refresh", AUTHENTICATION_API_PREFIX + "/**",
            OPEN_PREFIX + "/**", WHITE_PREFIX + "/**", "/error", "/health", "/static/**");


}
