# 基于mybatis-plus二次封装的era多租户生态开发框架

## 介绍
核心基于`mybatis-plus`,针对多租户数据隔离做了规范化处理

#### 使用方法
> 1. 主应用中调用`mybatis-plus`和`Hikari`配置,参考`bootstrap-db.yml`配置
> 2. service层继承[IMyBatisPlusService.java](src/main/java/com/ourexists/era/framework/orm/mybatisplus/service/IMyBatisPlusService.java), 
> 3. serviceImpl继承[AbstractMyBatisPlusService.java](src/main/java/com/ourexists/era/framework/orm/mybatisplus/service/AbstractMyBatisPlusService.java)
> 4. 租户隔离实体类继承[EraEntity.java](src/main/java/com/ourexists/era/framework/orm/mybatisplus/EraEntity.java)
> 5. 非隔离实体类继承[MainEntity.java](src/main/java/com/ourexists/era/framework/orm/mybatisplus/MainEntity.java)
> 6. [OrmUtils.java](src/main/java/com/ourexists/era/framework/orm/mybatisplus/OrmUtils.java)工具提供从`page`中抽取`Pagination`的公共方法
> 7. 租户隔离数据表需要以`t_xxx`规则命名
> 8. 租户编号规则。每个租户由3位编号构成，租户的下挂租户为上级租户编号+新的3位住户编号构成
> 9. 数据库租户隔离字段为`tenant_id`
> 10. `TenantDataAuth`中`lowerControlPower` 针对下级租户进行`OperatorModel`中的操作权限控制
> 11. 可通过`UserContext`方法设置对应的租户及权限级控制数据隔离权限


