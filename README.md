<div align="right">
  Language: 
  :us:
  <a title="Chinese" href="README-CN.md">:cn:</a>
</div>

# spring-cloud

[![author](https://img.shields.io/badge/author-mrtt-blue.svg)](https://jiangtj.gitlab.io/me)
[![email](https://img.shields.io/badge/email-jiang.taojie@foxmail.com-blue.svg)](mailto:jiang.taojie@foxmail.com)

一个轻量的能开箱即用的 Spring Cloud 微服务套件，集成 nacos，鉴权，监控，等一些通用的模块

### 版本
- java: jdk 17
- spring boot: 3.0.4
- spring cloud: 2022.0.1
- spring cloud alibaba: 2022.0.0.0-RC1
- spring boot admin: 3.0.1
- jjwt: 0.11.5

### 列表
- api-gateway: todo
- [nacos](https://github.com/nacos-group/nacos-docker)
- [sba-server](https://github.com/codecentric/spring-boot-admin):一个用于展示监控微服务
- base-reactive: 基础的 reactive 微服务，作为基本的业务服务单元
- base-servlet: 基础的 servlet 微服务，作为基本的业务服务单元
- micro-auth: 微服务内部鉴权模块，为每个内部微服务统一的鉴权
- micro-common: 微服务通用模块
  - 工具类
  - 统一的错误处理[RFC 7807](https://www.rfc-editor.org/rfc/rfc7807.html)

### nacos

不使用 spring cloud config 与 eureka，他们在实际项目中不怎么好用，不如[nacos](https://github.com/nacos-group/nacos-docker)，开发环境快速使用，运行想下面的命令

```shell
docker run --name nacos-quick -e MODE=standalone -p 8848:8848 -p 9848:9848 -d nacos/nacos-server:v2.2.0
```


