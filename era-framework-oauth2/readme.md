# 1.1.0
## 使用规则
1. Maven加入包依赖
   ```java
   <dependency>  
       <groupId>com.oruexists.era</groupId>  
       <artifactId>era-framework-oauth2</artifactId>  
       <version>1.0.0-SNAPSHOT</version>  
   </dependency>
   ```
2. 使用`@EnableEraAuthorizationServer`启动认证服务器。  
   使用`@EnableEraMixOauth2Server`启动认证兼资源服务器。
   使用`@EnableEraResourceServer`启动资源服务器。

3. 核心使用spring-security-oauth2框架体系

### 2023/01/30
* OAuth2资源服务器认证服务名等价应用服务名





