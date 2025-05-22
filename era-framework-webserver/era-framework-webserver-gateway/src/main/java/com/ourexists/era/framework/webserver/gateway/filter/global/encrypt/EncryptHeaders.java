/*
 * Copyright (c) 2025. created by ourexists.https://gitee.com/ourexists
 */

package com.ourexists.era.framework.webserver.gateway.filter.global.encrypt;

import com.ourexists.era.framework.core.EraSystemHeader;

public interface EncryptHeaders {

    /**
     * token header
     */
    String TOKEN_HEADER = EraSystemHeader.AUTH_HEADER;
    /**
     * 时间戳 header
     */
    String TIMESTAMP_HEADER = "Timestamp";
    /**
     * 签名 header
     */
    String SIGN_HEADER = "Sign";

}
