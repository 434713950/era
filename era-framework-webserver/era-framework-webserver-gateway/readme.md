# era-webserver网关体系

## 介绍及使用
1. 核心网关采用`spring-cloud-gateway`webflux,针对组件进行统一适配化的配置,网关配置参考其官网.额外提供能力如下:
   > * xss防护,配置`era.xss.enabled=true`开启
   > * 请求响应的数据体加密,`era.masking.enabled=true`启用并配置.相关配置见对应元数据文件
   > * 提供路由过滤器`OAuth2TokenRelay`,通过路由相关配置`clientRegistrationId`可实现对开放接口的OAuth2自动鉴权处理
   > * 提供路由过滤器`RouteUriBasePredicatePath`,可根据请求路径配置`parts`取出route,实现通过路径规则的负载路由.
2. 限流采用原生`ratelimiter-redis`.提供了以下几种限流方式:
   > * `remoteAddressKeyResolver`根据IP限流
   > * `apiKeyResolver`API接口限流
   > * `tenantKeyResolver`租户限流
3. 熔断组件集成了`resilience4j`.未默认使用,需要根据官方配置进行集成.额外提供了降级fallback`/circuitBreakerFallback`.

## 使用方式
参考配置文件[application-gateway-sample.yml](src/main/resources/application-gateway-sample.yml)

## 使用场景

1. web应用网关,api网关