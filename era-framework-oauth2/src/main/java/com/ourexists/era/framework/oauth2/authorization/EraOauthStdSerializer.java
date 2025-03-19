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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * @author pengcheng
 * @version 1.0.0
 * @date 2023/5/22 15:33
 * @since 1.0.0
 */
@Slf4j
public class EraOauthStdSerializer extends StdSerializer<EraOauthException> {

    protected EraOauthStdSerializer() {
        super(EraOauthException.class);
    }

    @Override
    public void serialize(EraOauthException e, JsonGenerator gen, SerializerProvider provider) throws IOException {
        log.error(e.getMessage(), e);
        String msg = e.getMessage();
        if (!StringUtils.isEmpty(msg) && msg.contains(":")) {
            msg = msg.split(":")[1];
        }
        gen.writeStartObject();
        gen.writeStringField("code", "500");
        gen.writeStringField("msg", msg);
        gen.writeEndObject();
    }
}
