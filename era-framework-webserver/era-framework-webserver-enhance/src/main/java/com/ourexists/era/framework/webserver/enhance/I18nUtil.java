/*
 * Copyright (c) 2025. created by ourexists.https://gitee.com/ourexists
 */

package com.ourexists.era.framework.webserver.enhance;

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
        I18nUtil.messageSource = messageSource;
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
