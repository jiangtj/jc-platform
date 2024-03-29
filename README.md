
# J Cloud Platform

![author](https://img.shields.io/badge/author-mrtt-blue.svg)
[![email](https://img.shields.io/badge/email-jiang.taojie@foxmail.com-blue.svg)](mailto:jiang.taojie@foxmail.com)
![status](https://img.shields.io/badge/status-developing-yellow.svg)
![Maven Central Version](https://img.shields.io/maven-central/v/com.jiangtj.platform/parent)

这是基于 Spring Cloud 的轻量的能开箱即用的微服务平台，集成 Polaris，鉴权，监控，等一些必要的通用模块，让你能快速的搭建微服务架构

[我还写了一些文章来介绍这个项目搭建过程，思路等，**不是文档**](https://jiangtj.com/tags/Spring-Cloud-%E5%B9%B3%E5%8F%B0%E6%90%AD%E5%BB%BA/)

### 主要版本与环境

- java: jdk 17
- database: mariadb 10
- framework: spring cloud 2023.x & spring cloud tencent 1.x-2023.x

### 使用

在开发阶段，建议使用本地安装。即 `clone` 后，`mvn install -P lib` 安装到本地

### 列表/计划

模块
- [micro-common](/micro-common): 通用的一些工具类，常量等
- [micro-web](/micro-web): Web 相关的一些工具类，常量等
- [micro-auth](/micro-auth): 对 Spring Boot 应用提供基础鉴权
- [micro-sql-jooq](/micro-sql-jooq): 为 jooq 提供业务层的封装
- [micro-sql-r2dbc](/micro-sql-r2dbc): 为 r2dbc 提供业务层的封装
- [micro-spring-boot-starter](/micro-spring-cloud-starter): 调整 Spring Boot 的一些默认配置，接口规范、校验、缓存等
- [micro-spring-cloud-starter](/micro-spring-cloud-starter): 对 J Cloud Platform 应用进行配置
- [micro-test](/micro-test): 提供对 micro-auth 模块测试的工具类
- [micro-test-cloud](/micro-test-cloud): 提供对 micro-auth 模块测试的工具类

服务
- [ ] gateway-session: session 网关，对外基于 session 的鉴权，[对应前端项目点击此处查看](https://github.com/jiangtj-lab/jc-admin-session)
- [ ] gateway-token: token 网关，对外基于 token 的鉴权，常用于 android/ios
- [ ] system-server: 系统服务，提供授权认证，管理员与角色管理等功能
- [ ] core-server:
  - [x] 获取并分享微服务公钥（有一定延迟，无感）
  - [ ] 单独创建RSA密钥（无延迟，需要提前配置）
- [x] [sba-server](https://github.com/codecentric/spring-boot-admin): 一个轻量的微服务监控服务
- [x] base-reactive: 基础的 reactive 微服务例子
- [x] base-servlet: 基础的 servlet 微服务例子

### 模型

![](https://github.com/jiangtj/jc-platform/assets/15902347/48c9a592-a314-4d7e-9838-5fc6528f8caf)

### 开发环境

#### 创建 北极星服务

北极星是集服务注册与发现，配置中心，流量控制等为一体的微服务治理平台，相对于eureka和spring cloud config来说，更简单与好用（修改常用端口为一些非常有端口）

```shell
docker run --name polaris \
--restart=unless-stopped \
-p 15010:15010 \
-p 8101:8101 \
-p 8100:8100 \
-p 18080:8080 \
-p 18090:8090 \
-p 18091:8091 \
-p 18093:8093 \
-p 18761:8761 \
-p 19090:9090 \
-d --privileged=true polarismesh/polaris-standalone:v1.17.2
```

#### 创建 System 服务 MySQL

```shell
docker run --name system-db \
--restart=unless-stopped \
-p 3311:3306 \
-e MYSQL_DATABASE=system-db \
-e MYSQL_ROOT_PASSWORD=123456 \
-e CHARACTER_SET_SERVER=utf8mb4 \
-e COLLATION_SERVER=utf8mb4_unicode_ci \
-d mariadb:10
```
