# 基于spring-data-mongodb针对租户数据隔离二次封装的ERA生态开发框架

## 介绍

核心基于`spring-boot-starter-data-mongodb`,具体配置方式参考官方文档

#### 使用方法

> 1. 实体对象继承[EraMongoEntity.java](src/main/java/com/ourexists/era/framework/orm/mongodb/EraMongoEntity.java)
     或[EraTenantMongoEntity.java](src/main/java/com/ourexists/era/framework/orm/mongodb/EraTenantMongoEntity.java)
> 2. 使用[EraMongoTemplate.java](src/main/java/com/ourexists/era/framework/orm/mongodb/EraMongoTemplate.java)实现数据库操作
> 3. 注意租户id规则。每个租户由3位编号构成，租户的下挂租户为上级租户编号+新的3位住户编号构成
> 4. 租户数据权限与ERA框架整体生态一致。`TenantDataAuth`中`lowerControlPower` 针对下级租户进行`OperatorModel`
     中的操作权限控制。租户0默认拥有所有数据权限
> 5. 数据库租户固定隔离字段为`tenant_id`

#### 使用场景

> 1. 配合era生态使用实现多租户数据隔离