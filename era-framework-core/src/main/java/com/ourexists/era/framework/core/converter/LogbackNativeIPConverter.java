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

package com.ourexists.era.framework.core.converter;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pengcheng
 * @date 2022/2/21
 * @since 1.0.0
 **/
public class LogbackNativeIPConverter extends ClassicConverter {
    private static final List<String> IP_CACHE = new ArrayList<>(2);

    @Override
    public String convert(ILoggingEvent event) {
        try {
            if (IP_CACHE.isEmpty()) {
                String localhost = InetAddress.getLocalHost().getHostAddress();
                IP_CACHE.add(localhost);
                return localhost;
            }
            return IP_CACHE.get(0);
        } catch (UnknownHostException e) {
            //日志配置,这里日志不输出
        }
        return "0.0.0.0";
    }
}
