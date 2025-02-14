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

package com.ourexists.era.framework.oauth2.authorization;

import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * @author pengcheng
 * @version 1.0.0
 * @date 2023/5/22 16:01
 * @since 1.0.0
 */
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = EraOauthStdSerializer.class)
public class EraOauthException extends OAuth2Exception {
    public EraOauthException(String msg) {
        super(msg);
    }

    public EraOauthException(String msg, Throwable t) {
        super(msg, t);
    }
}
