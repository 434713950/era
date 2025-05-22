package com.ourexists.era.framework.webserver.gateway.oauth2;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
public class OAuth2AuthorizedClientWrapper implements Serializable {

    private String clientRegistrationId;
    private String principalName;

    private String accessTokenValue;
    private Instant accessTokenIssuedAt;
    private Instant accessTokenExpiresAt;
    private String accessTokenType;

    private String refreshTokenValue;
    private Instant refreshTokenIssuedAt;

    public static OAuth2AuthorizedClientWrapper from(OAuth2AuthorizedClient client) {
        OAuth2AuthorizedClientWrapper wrapper = new OAuth2AuthorizedClientWrapper();
        wrapper.setClientRegistrationId(client.getClientRegistration().getRegistrationId());
        wrapper.setPrincipalName(client.getPrincipalName());
        wrapper.setAccessTokenValue(client.getAccessToken().getTokenValue());
        wrapper.setAccessTokenIssuedAt(client.getAccessToken().getIssuedAt());
        wrapper.setAccessTokenExpiresAt(client.getAccessToken().getExpiresAt());
        wrapper.setAccessTokenType(client.getAccessToken().getTokenType().getValue());
        if (client.getRefreshToken() != null) {
            wrapper.setRefreshTokenValue(client.getRefreshToken().getTokenValue());
            wrapper.setRefreshTokenIssuedAt(client.getRefreshToken().getIssuedAt());
        }
        return wrapper;
    }

    public OAuth2AuthorizedClient toAuthorizedClient(ClientRegistration registration) {
        OAuth2AccessToken accessToken = new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER,
                accessTokenValue,
                accessTokenIssuedAt,
                accessTokenExpiresAt
        );

        OAuth2RefreshToken refreshToken = refreshTokenValue != null
                ? new OAuth2RefreshToken(refreshTokenValue, refreshTokenIssuedAt)
                : null;
        return new OAuth2AuthorizedClient(registration, principalName, accessToken, refreshToken);
    }
}

