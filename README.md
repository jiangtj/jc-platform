
# J Cloud Platform

[![author](https://img.shields.io/badge/author-mrtt-blue.svg)](https://jiangtj.gitlab.io/me)
[![email](https://img.shields.io/badge/email-jiang.taojie@foxmail.com-blue.svg)](mailto:jiang.taojie@foxmail.com)
[![status](https://img.shields.io/badge/status-developing-yellow.svg)](mailto:jiang.taojie@foxmail.com)

这是基于 Spring Cloud 的轻量的能开箱即用的微服务平台，集成 Polaris，鉴权，监控，等一些必要的通用模块，让你能快速的搭建微服务架构

[我还写了一些文章来介绍这个项目搭建过程，思路等，**不是文档**](https://jiangtj.com/tags/Spring-Cloud-%E5%B9%B3%E5%8F%B0%E6%90%AD%E5%BB%BA/)

### 主要版本与环境

- java: jdk 17
- database: mariadb 10
- framework: spring cloud 2022.x & spring cloud tencent 1.x-2022.x

### 列表/计划

服务
- [x] gateway-session: session 网关，对外基于 session 的鉴权，[对应前端项目点击此处查看](https://github.com/jiangtj-lab/jc-admin-session)
- [ ] gateway-token: token 网关，对外基于 token 的鉴权，常用于 android/ios
- [ ] system-server: 系统服务，提供授权认证，管理员与角色管理等功能
- [x] [sba-server](https://github.com/codecentric/spring-boot-admin): 一个轻量的微服务监控服务
- [x] base-reactive: 基础的 reactive 微服务，作为基本的业务服务单元
- [x] base-servlet: 基础的 servlet 微服务，作为基本的业务服务单元

COMMON模块(micro-common)
- [x] 统一的错误处理[RFC 7807](https://www.rfc-editor.org/rfc/rfc7807.html)
- [ ] 其他工具类

AUTH模块(micro-auth)
- [x] 统一 Token 生成与验证
- [ ] 基于角色的访问控制（RBAC）

SQL模块(micro-sql)
- [x] 集成 liquibase 初始化数据库
- [x] r2dbc 的工具类 DbUtils

TEST模块(micro-test): 简化单元或集成测试

### 搭建北极星服务

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
-d --privileged=true polarismesh/polaris-standalone:v1.16.1
```

需要添加一个配置文件dev组auth.properties

![image](https://user-images.githubusercontent.com/15902347/229067145-e14ca261-fda5-4c10-ad0f-99cf4bb1c0f9.png)

### 创建 System 服务 MySQL

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
