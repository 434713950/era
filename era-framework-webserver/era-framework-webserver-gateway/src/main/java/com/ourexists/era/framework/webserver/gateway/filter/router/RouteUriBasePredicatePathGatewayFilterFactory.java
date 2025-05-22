package com.ourexists.era.framework.webserver.gateway.filter.router;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.route.Route;

import java.lang.reflect.Field;
import java.net.URI;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR;

/**
 * 根据路径映射uri过滤处理
 */
@Slf4j
public class RouteUriBasePredicatePathGatewayFilterFactory extends AbstractGatewayFilterFactory<RouteUriBasePredicatePathGatewayFilterFactory.Config> {

    public RouteUriBasePredicatePathGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getURI().getPath();
            String[] parts = path.split("/");
            if (parts.length >= 2) {
                String serviceName = parts[config.parts]; // 获取路径中的服务名
                Route route = exchange.getAttribute(GATEWAY_ROUTE_ATTR);
                if (route == null) {
                    return chain.filter(exchange);
                }
                String scheme = route.getUri().getScheme();
                try {
                    Field field = Route.class.getDeclaredField("uri");
                    field.setAccessible(true);
                    field.set(route, URI.create(scheme + "://" + serviceName));
                    field.setAccessible(false);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    log.error(e.getMessage());
                }
                exchange.getAttributes().put(GATEWAY_ROUTE_ATTR, route);
            }
            return chain.filter(exchange);
        };
    }

    public static class Config {

        private Integer parts = 1;

        public Integer getParts() {
            return parts;
        }

        public void setParts(Integer parts) {
            this.parts = parts;
        }
    }
}
