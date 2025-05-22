package com.ourexists.era.framework.webserver.gateway.oauth2;

import com.alibaba.fastjson2.JSON;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class RedisReactiveOAuth2AuthorizedClientService implements ReactiveOAuth2AuthorizedClientService {

    private final ReactiveRedisTemplate<String, String> redisTemplate;

    private final ReactiveClientRegistrationRepository clientRegistrationRepository;
    private final String redisPrefix = "satellite:oauth2:client:";

    public RedisReactiveOAuth2AuthorizedClientService(ReactiveRedisTemplate<String, String> redisTemplate,
                                                      ReactiveClientRegistrationRepository clientRegistrationRepository) {
        this.redisTemplate = redisTemplate;
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @Override
    public <T extends OAuth2AuthorizedClient> Mono<T> loadAuthorizedClient(String clientRegistrationId, String principalName) {
        String key = buildKey(clientRegistrationId, principalName);
        return redisTemplate.opsForValue().get(key)
                .flatMap(json -> {
                    OAuth2AuthorizedClientWrapper wrapper = JSON.parseObject(json, OAuth2AuthorizedClientWrapper.class);
                    return clientRegistrationRepository.findByRegistrationId(clientRegistrationId)
                            .map(reg -> (T) wrapper.toAuthorizedClient(reg));
                });
    }

    @Override
    public Mono<Void> saveAuthorizedClient(OAuth2AuthorizedClient authorizedClient, Authentication principal) {
        OAuth2AuthorizedClientWrapper wrapper = OAuth2AuthorizedClientWrapper.from(authorizedClient);
        String key = buildKey(authorizedClient.getClientRegistration().getRegistrationId(), authorizedClient.getPrincipalName());
        Duration ttl = Duration.between(
                wrapper.getAccessTokenIssuedAt(),
                wrapper.getAccessTokenExpiresAt()
        );
        String json = JSON.toJSONString(wrapper);
        return redisTemplate.opsForValue().set(key, json, ttl).then();
    }

    @Override
    public Mono<Void> removeAuthorizedClient(String clientRegistrationId, String principalName) {
        String key = buildKey(clientRegistrationId, principalName);
        return redisTemplate.delete(key).then();
    }

    private String buildKey(String clientId, String principalName) {
        return redisPrefix + clientId + ":" + principalName;
    }
}
