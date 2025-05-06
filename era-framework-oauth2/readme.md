# 基于spring-OAuth2实现的一套ERA框架生态的认证体系框架

## 介绍

基于`spring-cloud-starter-oauth2`的二次封装，用于ERA生态的框架实现配合

# 1.1.0
#### 使用方法
1. 使用`@EnableEraAuthorizationServer`启动认证服务器。  
2. 使用`@EnableEraMixOauth2Server`启动认证兼资源服务器。
3. 使用`@EnableEraResourceServer`启动资源服务器。

#### 使用场景
* 参考`spring-cloud-starter-oauth2`