# 1.1.0
## 使用规则
1. 使用方式参考[era-framework-tms-core](../era-framework-tms-core)

实现：
1. 将默认的票据管理器由内存管理迁移为redis管理。利用redission的锁特性处理分布式冲突问题
