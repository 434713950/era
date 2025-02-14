# 1.1.0
### 2024/04/19
* 提供getOne()方法的性能高效方法

### 2023/03/27
* 添加跳过主租户的强制清除策略。使用`UserContext.getTenantInfo().setSkipMain(false)`实现。

### 2022/07/25
* 租户插件将只针对`t_xxx`的表生效
* 提供简易数据权限能力，使用`EraDataAccessAuth`进行设置。
> 注意租户id规则。每个租户由3位编号构成，租户的下挂租户为上级租户编号+新的3位住户编号构成  
> `TenantDataAuth`中`lowerControlPower` 针对下级租户进行`OperatorModel`中的操作权限控制
> 租户0默认控制所有数据权限

### 2022/07/15
* mybatis-plus 升级为3.5.2,注意与原版本不兼容，需对代码进行调整
* 集成mybatis-plus租户组件进行sql重组方式的数据隔离，清除原基于EraWrapper的租户数据隔离模式

# 1.0.0
### 2022/04/19
1. 新增`EraEntity`,配合`EntityWrapper`增强方法`EraWrapper`使用。默认使用了租户的条件限制，规范化租户条件。
> 数据库要求主表新增固定字段tenant_id

### 2022/04/01
* 提供了mybatis+mybatis-plus数据源及其工具
* 使用了Hikari连接池

> ## 使用规则
> 1. Maven加入包依赖  
>    ```java
>    <dependency>  
>        <groupId>com.ourexists.era</groupId>  
>        <artifactId>era-framework-orm-mybatisplus</artifactId>  
>    </dependency>
>    ```
> 2. 主应用中调整mybatis-plus和Hikari配置,参考`bootstrap-db.yml`配置
> 3. service层继承`service.com.ourexists.era.framework.orm.mybatisplus.IMyBatisPlusService`, 同时serviceImpl继承`service.com.ourexists.era.framework.orm.mybatisplus.AbstractMyBatisPlusService`使用mybatisPlus特性



