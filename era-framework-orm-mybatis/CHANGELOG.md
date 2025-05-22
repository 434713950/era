# Change Log

## [1.1.0] - <2024-04-19>

### Added

* 提供getOne()方法的性能高效方法

## [1.1.0] - <2023-03-27>

### Added

* 添加跳过主租户的强制清除策略。使用`UserContext.getTenantInfo().setSkipMain(false)`实现。

## [1.1.0] - <2022-07-25>

### Added

* 租户插件将只针对`t_xxx`的表生效
* 提供简易数据权限能力，使用`EraDataAccessAuth`进行设置。

## [1.1.0] - <2022-07-15>

### Added

* `mybatis-plus`升级为3.5.2,注意与原版本不兼容，需对代码进行调整
* 集成`mybatis-plus`租户组件进行sql重组方式的数据隔离

### Removed

* 清除原基于`EraWrapper`的租户数据隔离模式

## [1.1.0] - <2022-04-19>

### Added

* 新增`EraEntity`,配合`EntityWrapper`增强方法`EraWrapper`使用。默认使用了租户的条件限制，规范化租户条件。
* 数据库要求主表新增固定字段`tenant_id`

## [1.1.0] - <2021-04-01>

### Added

* 提供了`mybatis+mybatis-plus`数据源及其工具
* 使用了`Hikari`连接池

[1.1.0]: <>