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

package com.ourexists.era.framework.webmvc.configure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author pengcheng
 * @date 2022/4/22 14:42
 * @since 1.0.0
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "era.security.cors")
public class CorsProperties {

    private List<String> allowedOrigins = Collections.singletonList("*");


    private List<String> allowedMethods = Arrays.asList("GET", "POST", "DELETE", "PUT", "OPTIONS");

    private List<String> allowedHeaders = Collections.singletonList("*");

    private Boolean allowCredentials = true;
}
