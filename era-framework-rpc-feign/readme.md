# 1.1.0
## 使用规范
1. `@FeignClient`中添加相关`configuration`配置
2. 使用`SimpleAuthFeignConfiguration`时同步实现`SimpleAuthRequestManager`
3. 使用`HeaderFeignConfiguration`无需额外配置

### init
* 提供`HeaderFeignConfiguration`用于feign直接对接认证权限头的信息请求
* 提供`SimpleAuthRequestManager`用于门户层feign直接调用基础层的数据请求



