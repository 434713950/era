# 1.1.0

## 使用规则

* 同1.0.0，升级版本

### 2024/02/23

* 接口日志输出跳过文件媒体类型的参数打印

### 2022/04/26

* 修复统一异常反参未生效问题

### 2022/04/22

* 新增cors配置化能力。配置前缀`era.security.cors`。默认放开所有的路径

---
# 1.0.0

## 使用规则

1. Maven加入包依赖
   ```java
   <dependency>  
       <groupId>com.ourexists.era</groupId>  
       <artifactId>era-framework-webmvc</artifactId>  
       <version>1.0.0-SNAPSHOT</version>  
   </dependency>
   ```
2. controller层继承`controller.com.ourexists.era.framework.webmvc.IController`






