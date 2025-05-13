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

package com.ourexists.era.framework.webmvc;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 国际化工具
 *
 * @author pengCheng
 * @date 2018/5/20
 */
public class I18nUtil {

    private static MessageSource messageSource;

    public I18nUtil(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public static String i18nParser(String i18nMsg, String... text) {
        String msg = i18nMsg;
        if (messageSource != null && msg.contains("${")) {
            try {
                msg = messageSource.getMessage(i18nMsg.substring(2, i18nMsg.length() - 1), null, LocaleContextHolder.getLocale());
            } catch (NoSuchMessageException x) {
                //nothing
            }
        }
        if (text != null && text.length > 0 && !StringUtils.isEmpty(msg)) {
            for (int i = 0; i < text.length; i++) {
                msg = msg.replace("{" + i + "}", text[i]);
            }
        }
        return msg;
    }

}
