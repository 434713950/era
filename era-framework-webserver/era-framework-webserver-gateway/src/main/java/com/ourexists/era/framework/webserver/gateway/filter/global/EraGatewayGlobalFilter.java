/*
 * Copyright (c) 2025. created by ourexists.https://gitee.com/ourexists
 */

package com.ourexists.era.framework.webserver.gateway.filter.global;

import com.ourexists.era.framework.core.PathRule;
import com.ourexists.era.framework.webserver.gateway.filter.global.encrypt.MaskingProperties;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

public abstract class EraGatewayGlobalFilter implements GlobalFilter, Ordered {

    protected AntPathMatcher antPathMatcher;

    protected MaskingProperties maskingProperties;

    public EraGatewayGlobalFilter(AntPathMatcher antPathMatcher,
                                  MaskingProperties maskingProperties) {
        this.antPathMatcher = antPathMatcher;
        this.maskingProperties = maskingProperties;
    }

    /**
     * 白名单
     *
     * @param exchange
     * @return
     */
    public boolean isIgnore(ServerWebExchange exchange) {
        List<String> ignoreUrls = maskingProperties.getIgnoreUrls();
        for (String herderWhitePath : PathRule.HERDER_WHITE_PATHS) {
            ignoreUrls.add("/*" + herderWhitePath);
        }
        String path = exchange.getRequest().getURI().getPath();
        for (String url : ignoreUrls) {
            if (antPathMatcher.match(url, path)) {
                return true;
            }
        }
        return false;
    }
}
