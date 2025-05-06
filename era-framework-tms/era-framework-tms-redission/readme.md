# 票据管理器框架-redission管理机制

## 介绍
使用redission作为存储器的票据管理器。自动化集成。将默认的票据管理器由内存管理迁移为redis管理。利用redission的锁特性处理分布式冲突问题

#### 使用方法
> 1. 使用方式参考[era-framework-tms-core](../era-framework-tms-core)

#### 使用场景
> 1. 分布式情形下的票据管理