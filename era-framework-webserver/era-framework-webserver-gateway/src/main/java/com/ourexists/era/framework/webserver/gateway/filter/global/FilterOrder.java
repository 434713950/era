/*
 * Copyright (c) 2025. created by ourexists.https://gitee.com/ourexists
 */

package com.ourexists.era.framework.webserver.gateway.filter.global;

import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.core.Ordered;

public interface FilterOrder {

    int CACHE_REQUEST_FILTER_ORDER = Ordered.HIGHEST_PRECEDENCE;

    int DECRYPT_REQUEST_FILTER_ORDER = CACHE_REQUEST_FILTER_ORDER + 1;

    int XSS_REQUEST_FILTER_ORDER = DECRYPT_REQUEST_FILTER_ORDER + 1;

    int ENCRYPT_RESPONSE_FILTER_ORDER = NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER - 1;

}
