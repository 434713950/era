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

| Era Framework Version | 变更内容 | Spring Cloud Version | Spring Cloud Alibaba Version | Spring Boot Version |  
| :-----                |:-----| :----                |  :----                       | :----               |   
| 1.1.0                 | 无    | Hoxton.SR12          |  2.2.7.RELEASE               | 2.3.12.RELEASE      |


框架结构
-----------------------------------
>具体使用方式可见相关模块的readme.md
```
┌─[era-boot-starter-web](era-boot-starter-web) 资源服务器快速启动模块
│
├─[era-framework-cache](era-framework-cache) 缓存管理模块
│
├─[era-framework-core](era-framework-core) 框架核心包
│
├─[era-framework-dependencies](era-framework-dependencies) 框架依赖管理包
│
├─[era-framework-distribution](era-framework-distribution) 分布式事务相关的集成包(seata为核心)
│
├─[era-framework-excel](era-framework-excel) excel管理相关,核心使用easy-excel,主要集成处理了分片导出与OSS的协调
│
├─[era-framework-idempotent](era-framework-idempotent) 幂等处理
│
├─[era-framework-mq-rocketmq](era-framework-mq-rocketmq) rocketmq集成包
│
├─[era-framework-oauth2](era-framework-oauth2) 集成oath2的era认证体系
│
├─[era-framework-orm-mongodb](era-framework-orm-mongodb) 集成mongodb二次开发框架
│
├─[era-framework-orm-mybatisplus](era-framework-orm-mybatisplus) 集成mybatis-plus二次开发框架
│
├─[era-framework-oss](era-framework-oss) oss存储管理
│
├─[era-framework-rpc-feign](era-framework-rpc-feign) feign扩展开发框架
│
├─[era-framework-scheduler](era-framework-scheduler) 任务调度
│ ├─[era-framework-scheduler-core](era-framework-scheduler/era-framework-scheduler-core) 任务调度核心管理包
│ ├─[era-framework-scheduler-schedulerx2](era-framework-scheduler/era-framework-scheduler-schedulerx2) 阿里schedulerx2调度集成（暂未实现）
│ └─[era-framework-scheduler-xxljob](era-framework-scheduler/era-framework-scheduler-xxljob) xxljob调度集成
│
├─[era-framework-sharing-jdbc](era-framework-sharing-jdbc) 基于sharingjdbc的数据库分片
│
└─[era-framework-webmvc](era-framework-webmvc)  mvc相关规范扩展框架
```

捐赠
----
如果您觉得本项目还可以，请点个star~