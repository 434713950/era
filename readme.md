era  分布式集成框架
===============

[![AUR](https://img.shields.io/badge/license-AGPL%203.0-blue.svg)]()
[![](https://img.shields.io/badge/Author-ourexists-orange.svg)]()
[![](https://img.shields.io/badge/version-1.1.0-brightgreen.svg)]()

介绍
-----------------------------------
基于springboot生态以及租户数据管理理念集成二次封装的分布式框架

交流与支持
-----------------------------------

- 微信： m15026681077
- 邮件： 434713950@163.com

版本对应表

| Era Framework Version | 变更内容     | Spring Cloud Version | Spring Cloud Alibaba Version | Spring Boot Version | JAVA Version |
|:----------------------|:---------|:---------------------|:-----------------------------|:--------------------|:-------------|           
| 1.1.0                 | 无        | Hoxton.SR12          | 2.2.7.RELEASE                | 2.3.12.RELEASE      | 8+           |
| 2024.0.1              | 升级生态核心版本 | 2024.0.1             | 2024.0.1                     | 3.4.5               | 21           |

框架结构
-----------------------------------
> 具体使用方式可见相关模块的readme.md

┌─[era-boot-starter-web](era-boot-starter-web) 资源服务器快速启动模块 [readme.md](era-boot-starter-web/readme.md)  
│   
├─[era-framework-cache](era-framework-cache) 缓存管理模块 [readme.md](era-framework-cache/readme.md)  
│   
├─[era-framework-core](era-framework-core) 框架核心包 [readme.md](era-framework-core/readme.md)    
│   
├─[era-framework-dependencies](era-framework-dependencies) 框架依赖管理包    
│   
├─[era-framework-distribution](era-framework-distribution) 分布式事务相关的集成包(seata为核心) [readme.md](era-framework-distribution/readme.md)  
│   
├─[era-framework-excel](era-framework-excel) excel处理  [readme.md](era-framework-excel/readme.md)  
│   
├─[era-framework-idempotent](era-framework-idempotent) 幂等处理 [readme.md](era-framework-idempotent/readme.md)   
│   
├─[era-framework-oauth2](era-framework-oauth2) 集成oath2的era认证体系 [readme.md](era-framework-oauth2/readme.md)   
│ ├─[era-framework-oauth2-authorization](era-framework-oauth2/era-framework-oauth2-authorization)[era-framework-scheduler-core](era-framework-scheduler/era-framework-scheduler-core) 认证服务器   
│ ├─[era-framework-scheduler-schedulerx2](era-framework-scheduler/era-framework-scheduler-schedulerx2)[era-framework-oauth2-core](era-framework-oauth2/era-framework-oauth2-core) 核心包   
│ └─[era-framework-oauth2-resource](era-framework-oauth2/era-framework-oauth2-resource) 资源服务器    
│   
├─[era-framework-orm-mongodb](era-framework-orm-mongodb) 集成mongodb二次开发框架 [readme.md](era-framework-orm-mongodb/readme.md)   
│   
├─[era-framework-orm-mybatisplus](era-framework-orm-mybatisplus) 集成mybatis-plus二次开发框架 [readme.md](era-framework-orm-mybatisplus/readme.md)   
│   
├─[era-framework-oss](era-framework-oss) oss存储管理 [readme.md](era-framework-oss/readme.md)   
│   
├─[era-framework-rpc-feign](era-framework-rpc-feign) feign扩展开发框架 [readme.md](era-framework-rpc-feign/readme.md)   
│   
├─[era-framework-scheduler](era-framework-scheduler) 任务调度   
│ ├─[era-framework-scheduler-core](era-framework-scheduler/era-framework-scheduler-core) 任务调度核心管理包   
│ ├─[era-framework-scheduler-schedulerx2](era-framework-scheduler/era-framework-scheduler-schedulerx2)
阿里schedulerx2调度集成（暂未实现）[readme.md](era-framework-scheduler/era-framework-scheduler-schedulerx2/readme.md)      
│ └─[era-framework-scheduler-xxljob](era-framework-scheduler/era-framework-scheduler-xxljob) xxljob调度集成 [readme.md](era-framework-scheduler/era-framework-scheduler-xxljob/readme.md)     
│   
├─[era-framework-tms](era-framework-tms) 三方票据管理器   
│ ├─[era-framework-tms-core](era-framework-tms/era-framework-tms-core) 票据管理器核心包 [readme.md](era-framework-tms/era-framework-tms-core/readme.md)    
│ └─[era-framework-tms-redission](era-framework-tms/era-framework-tms-redission) 票据管理器redission集成包 [readme.md](era-framework-tms/era-framework-tms-redission/readme.md)    
│    
├─[era-framework-txflow](era-framework-txflow) 事务工作流 [readme.md](era-framework-txflow/readme.md)    
│   
├─[era-framework-sharing-jdbc](era-framework-sharing-jdbc) 基于sharingjdbc的数据库分片 [readme.md](era-framework-sharing-jdbc/readme.md)   
│   
└─[era-framework-webmvc](era-framework-webmvc)  mvc相关规范扩展框架 [readme.md](era-framework-webmvc/readme.md)

![era整体生态.png](era%E6%95%B4%E4%BD%93%E7%94%9F%E6%80%81.png)

捐赠
----
如果您觉得本项目还可以，请点个star~