# 说明

OSS集成适配器，支持minio和AliOss的一键化配置

# 1.1.0

## 使用规则

1. 使用[OssTemplate.java](src/main/java/com/ourexists/era/framework/oss/OssTemplate.java)模板进行方法调用。
2. 配置见文件[spring-configuration-metadata.json](src/main/resources/META-INF/spring-configuration-metadata.json)

### 2024/04/08

* 提供访问签名模式支持