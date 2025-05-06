# 票据管理器框架

## 介绍
根据市面常用的三方接口调用规范汇总，集成的一套票据快速管理框架

#### 使用方法
> 1. 实现请求器[RemoteTokenRequester.java](src/main/java/com/ourexists/era/framework/tms/core/requester/RemoteTokenRequester.java),框架会根据请求器关联的连接名寻址执行
> 2. 连接信息[Connection.java](src/main/java/com/ourexists/era/framework/tms/core/requester/Connection.java)中的`name`需要保证唯一性
> 3. 使用管理器对所有的票据管理[EraThirdAccessTokenManger.java](src/main/java/com/ourexists/era/framework/tms/core/manager/EraThirdAccessTokenManger.java),该管理器实现了对所有的票据管理
> 4. 项目正式使用中可以通过不同的`connection`管理模式进行连接信息管理。

#### 使用场景
> 1. 各类三方请求的认证票据管理
> 2. 默认使用内存管理方式进行的票据管理，只适用于`单体项目`的情形。分布式会存在票据互刷的情况，导致管理理念失效。
> 3. 如需使用分布式管理机制请使用[era-framework-tms-redission](../era-framework-tms-redission)

#### 框架模型
![model.png](model.png)

#### 场景思路
![apply.png](apply.png)