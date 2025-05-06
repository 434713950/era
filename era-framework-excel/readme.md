# 基于easy-excel的分页导出快速处理框架

## 介绍

基于easy-excel,针对导出的常态化场景进行二次封装，简化开发成本

#### 使用方法

> 1. sdk集成easy-excel以及[era-framework-oss](../era-framework-oss)实现
> 2. 继承[AbstractExportExecutor.java](src/main/java/com/ourexists/era/framework/excel/AbstractExportExecutor.java)
     实现具体数据获取方法，调用`export()`方法进行导出

#### 使用场景

> 1. 搭配分页查询实现分片导出。

## 更新日志

### 1.1.0

#### 2025/02/01

> 1.实现分片导出及上传，解决内存高占用问题   
> 2.集成了最常用的一些excel样式控件 

