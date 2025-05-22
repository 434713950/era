/*
 * Copyright (c) 2025. created by ourexists.https://gitee.com/ourexists
 */

package com.ourexists.era.framework.webserver.gateway.filter;

import com.ourexists.era.framework.webserver.gateway.filter.global.encrypt.CacheRequestBodyGlobalFilter;
import com.ourexists.era.framework.webserver.gateway.filter.global.encrypt.MaskingProperties;
import com.ourexists.era.framework.webserver.gateway.filter.global.xss.XssRequestGlobalFilter;
import com.ourexists.era.framework.webserver.gateway.filter.router.OAuth2TokenRelayGatewayFilterFactory;
import com.ourexists.era.framework.webserver.gateway.filter.router.RouteUriBasePredicatePathGatewayFilterFactory;
import org.springframework.context.annotation.Import;

@Import({CacheRequestBodyGlobalFilter.class,
        MaskingProperties.class,
        XssRequestGlobalFilter.class,
        OAuth2TokenRelayGatewayFilterFactory.class,
        RouteUriBasePredicatePathGatewayFilterFactory.class
})
public class EraGatewayFilterConfigurer {

}
