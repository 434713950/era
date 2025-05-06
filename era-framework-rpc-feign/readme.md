# era生态feign组件框架

## 介绍
基于spring-feign以及era生态规范的一套框架

#### 使用方法
> 1. `@FeignClient`中添加相关`configuration`配置
> 2. 使用`SimpleAuthFeignConfiguration`时同步实现`SimpleAuthRequestManager`，用于聚合服务feign直接调用基础层的数据请求
> 3. 使用`HeaderFeignConfiguration`无需额外配置，用于feign直接对接认证权限头的信息请求

#### 使用场景
> 1. 用于era服务内部的简易调用，减少认证资源调度损耗



