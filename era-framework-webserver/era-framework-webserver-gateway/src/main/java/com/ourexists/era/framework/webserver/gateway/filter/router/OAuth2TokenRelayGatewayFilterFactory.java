package com.ourexists.era.framework.webserver.gateway.filter.router;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
public class OAuth2TokenRelayGatewayFilterFactory extends AbstractGatewayFilterFactory<OAuth2TokenRelayGatewayFilterFactory.Config> {

    private final ReactiveOAuth2AuthorizedClientManager clientManager;

    public OAuth2TokenRelayGatewayFilterFactory(ReactiveOAuth2AuthorizedClientManager clientManager) {
        super(Config.class);
        this.clientManager = clientManager;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                    .withClientRegistrationId(config.clientRegistrationId)
                    .principal(config.clientRegistrationId)
                    .build();
            return clientManager.authorize(authorizeRequest)
                    .map(OAuth2AuthorizedClient::getAccessToken)
                    .map(OAuth2AccessToken::getTokenValue)
                    .defaultIfEmpty("")
                    .flatMap(token -> {
                        if (!token.isEmpty()) {
                            ServerHttpRequest request = exchange.getRequest().mutate()
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                    .build();
                            ServerWebExchange mutatedExchange = exchange.mutate().request(request).build();
                            return chain.filter(mutatedExchange);
                        }
                        return chain.filter(exchange);
                    });
        };
    }


    public static class Config {

        private String clientRegistrationId;

        public String getClientRegistrationId() {
            return clientRegistrationId;
        }

        public void setClientRegistrationId(String clientRegistrationId) {
            this.clientRegistrationId = clientRegistrationId;
        }
    }
}
