# 针对rocketmq封装的era生态规范化框架

## 介绍
基于spring-cloud-stream-binder-rocketmq框架基础封装

#### 使用方法
> 1. 推荐所有的mq消息体统一继承[BaseMqMessage.java](../era-framework-core/src/main/java/com/ourexists/era/framework/core/model/BaseMqMessage.java)
> 2. 属性`idempotentId`可配合框架era-framework-distribution中的`@Idempotent`默认参数搭配
> 3. 属性`sequenceKey`将配合配置中的顺序队列配置使用

#### 使用场景
> 1. 正常`rocketmq`使用场景即可,推荐参考使用方式形成规范
