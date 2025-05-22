# era-webserver生态认证服务

## 介绍和使用
1. 基于`spring-security-oauth2-authorization-server`的适配增强性管理.总体配置参考其官网,增强能力如下:
   > * 集成的自定义redis token管理机制,配置`era.token.store-type`选用对应的token管理机制
   > * 同时开启认证服务能力和资源服务能力.令认证服务器可以同时作为资源服务进行扩展性接口提供.
   > * 封装`EraAuthenticationProvider`和`EraAuthenticationConverter`,后续如需扩展自定义授权类型,只要实现注入Bean.

### 使用场景
* era-webserver生态的认证服务器,适配分布式情形