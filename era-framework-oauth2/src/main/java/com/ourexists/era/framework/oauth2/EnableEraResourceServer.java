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

import com.ourexists.era.framework.oauth2.resource.ResourceServerConfiguration;
import com.ourexists.era.framework.oauth2.token.TokenConfigurer;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * era体系认证服务启动器
 *
 * @author pengcheng
 * @date 2022/4/11 19:04
 * @since 1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({PublicServerConfigurer.class,
        TokenConfigurer.class,
        ResourceServerConfiguration.class})
public @interface EnableEraResourceServer {
}
