
# Spring Cloud 平台搭建

[![author](https://img.shields.io/badge/author-mrtt-blue.svg)](https://jiangtj.gitlab.io/me)
[![email](https://img.shields.io/badge/email-jiang.taojie@foxmail.com-blue.svg)](mailto:jiang.taojie@foxmail.com)

一个轻量的能开箱即用的 Spring Cloud 微服务套件，集成 nacos，鉴权，监控，等一些通用的模块

我还写了一些文章来介绍这个项目，[点击查看搭建过程](https://jiangtj.com/tags/Spring-Cloud-%E5%B9%B3%E5%8F%B0%E6%90%AD%E5%BB%BA/)

### 版本
- java: jdk 17
- spring boot: 3.0.4
- spring cloud: 2022.0.1
- spring cloud alibaba: 2022.0.0.0-RC1
- spring boot admin: 3.0.1
- jjwt: 0.11.5

### 列表
- api-gateway: todo
- [polaris-server](https://polarismesh.cn/)
- [sba-server](https://github.com/codecentric/spring-boot-admin):一个用于展示监控微服务
- base-reactive: 基础的 reactive 微服务，作为基本的业务服务单元
- base-servlet: 基础的 servlet 微服务，作为基本的业务服务单元
- micro-auth: 微服务内部鉴权模块，为每个内部微服务统一的鉴权
- micro-common: 微服务通用模块
  - 工具类
  - 统一的错误处理[RFC 7807](https://www.rfc-editor.org/rfc/rfc7807.html)

### 搭建北极星服务

```shell
docker run -d --privileged=true \
-p 15010:15010 \
-p 8101:8101 \
-p 8100:8100 \
-p 18080:8080 \
-p 18090:8090 \
-p 18091:8091 \
-p 18093:8093 \
-p 18761:8761 \
-p 19090:9090 polarismesh/polaris-server-standalone:latest
```

