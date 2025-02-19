# 1.1.0
## 2022/05/13
* 描述
> 1. 基于spring-cloud-stream-binder-rocketmq框架基础封装

* 使用说明
> 1.所有的mq消息体统一继承`BaseMqMessage`
>> 属性`idempotentId`可配合框架era-framework-distribution中的`@Idempotent`默认参数搭配
>> 属性`sequenceKey`将配合配置中的顺序队列配置使用

