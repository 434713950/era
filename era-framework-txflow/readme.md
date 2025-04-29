# 1.1.0

## 描述
一套简易事务流框架，应用于小型场景,可实现一套事务流的流程化记载，流程失败快速重处理机制

## 应用场景
1. 远程数据同步及推送
2. 单体架构下的简易流程控制管理

## 使用规则
1. 实现存储器[TxStore.java](src/main/java/com/ourexists/era/txflow/TxStore.java),通过自定义使用缓存还是数据库
2. 实现管理器[TxManager.java](src/main/java/com/ourexists/era/txflow/TxManager.java),自定义不同流程化操作.
3. `TxManager`中的`execute`方法为启用入口, `breakpointProcess`为断点重入方法

框架模型
> 待补充    