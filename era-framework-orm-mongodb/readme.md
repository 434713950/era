# 1.1.0
> ## 使用规则
> 1. Maven加入包依赖
>    ```java
>    <dependency>  
>        <groupId>com.wondersgroup.era</groupId>  
>        <artifactId>era-framework-orm-mongodb</artifactId>  
>    </dependency>

### 2022/08/18
* 提供`mongoTemplate`传入`tenantId`条件为主租户时抑制租户的能力

### 2022/07/29
* 使用spring-boot-starter-data-mongodb,具体配置方式参考官方文档
* 正常注入`mongoTemplate`使用。
* 提供了`EraMongoEntity`,`EraTenantMongoEntity`公共实体用于继承
* 租户能力将只针对拥有`tenantId`字段的实体生效。其中`Aggregate`,`findAll`相关操作不支持租户能力
* 提供简易数据权限能力，使用`EraDataAccessAuth`进行设置。
> 注意租户id规则。每个租户由3位编号构成，租户的下挂租户为上级租户编号+新的3位住户编号构成  
> `TenantDataAuth`中`lowerControlPower` 针对下级租户进行`OperatorModel`中的操作权限控制
> 租户0默认控制所有数据权限




