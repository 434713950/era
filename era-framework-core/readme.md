**era框架的核心功能包**
# 1.1.0
### 2023/09/04
* PageQuery取值根据属性`requirePage`自动变化。无需业务手动判断。

### 2023/01/30
* UserInfo内容扩充

---
# 1.0.0

### 2022/08/16
* 提供Idworker工具包

## 使用规则
1. 提供了部分通用utils。直接查看代码
2. 提供了当前用户获取能力。使用`UserContext`获取。该能力需要在集成**era-framework-oauth2**资源服务器能力后生效。
3. logback的规范，直接copy`logback-spring.xml`文件进入项目。配置参数查看`resources/META-INF/spring-configuration-metadata.json`

