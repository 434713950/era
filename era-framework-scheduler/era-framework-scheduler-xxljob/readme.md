使用方式
1. Maven加入包依赖  
```java
<dependency>  
    <groupId>com.wondersgroup.era</groupId>  
    <artifactId>xxljob-remote</artifactId>  
    <version>1.0.0-SNAPSHOT</version>  
</dependency>
``` 

2. 使用`@EnableXxlJobRemote`注解可开启对xxljob的远程控制。注入`XxlJobOpreateTemplate`对对应接口进行调用



附：
1. 账户密码使用xxl-job控制台给开出来的有权限的登陆用的账户密码。
2. 执行器id的获取方式。推荐用到新增接口的，执行器id自己写配置文件模式维护，各环境id不一致。
