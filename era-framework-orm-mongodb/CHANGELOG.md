# Change Log

## [1.1.0] - <2022-08-18>

### Added

* 提供`mongoTemplate`传入`tenantId`条件为主租户时抑制租户的能力

## [1.1.0] - <2022-07-29>

### Added

* 提供了`EraMongoEntity`,`EraTenantMongoEntity`公共实体用于继承
* 租户能力将只针对拥有`tenantId`字段的实体生效。其中`Aggregate`,`findAll`相关操作不支持租户能力
* 提供简易数据权限能力，使用`EraDataAccessAuth`进行设置。

[1.1.0]: <>