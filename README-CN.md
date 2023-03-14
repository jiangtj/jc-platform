<div align="right">
  语言: 
  <a title="英文" href="README.md">:us:</a>
  :cn:
</div>

# spring-cloud

[![author](https://img.shields.io/badge/author-mrtt-blue.svg)](https://jiangtj.gitlab.io/me)
[![email](https://img.shields.io/badge/email-jiang.taojie@foxmail.com-blue.svg)](mailto:jiang.taojie@foxmail.com)

一个能开箱即用的微服务例子，继承nacos，鉴权，监控，等一些通用的模块

### 版本
- java: jdk 17
- spring boot: 3.0.4
- spring cloud: 2022.0.1
- spring cloud alibaba: 2022.0.0.0-RC1
- spring boot admin: 3.0.1

### 列表
- api-gateway
- [nacos](https://github.com/nacos-group/nacos-docker)
- [admin-server](https://github.com/codecentric/spring-boot-admin):一个用于展示端点信息的第三方UI
- base-client: 基础的eureka客户端
- feign-client: 声明式的REST客户端
- config-client: 一个从config-server服务获取配置信息的例子

### nacos

不使用 spring cloud config 与 eureka，他们在实际项目中不怎么好用，不如[nacos](https://github.com/nacos-group/nacos-docker)，开发环境快速使用，运行想下面的命令

```shell
docker run --name nacos-quick -e MODE=standalone -p 8848:8848 -p 9848:9848 -d nacos/nacos-server:v2.2.0
```

### 请求链路
需要[zipkin服务](https://github.com/openzipkin/zipkin)
```bash
curl -sSL https://zipkin.io/quickstart.sh | bash -s
java -jar zipkin.jar
```


