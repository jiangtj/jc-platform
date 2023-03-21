<div align="right">
  Language: 
  :us:
  <a title="Chinese" href="README-CN.md">:cn:</a>
</div>

# Spring-Cloud

[![author](https://img.shields.io/badge/author-mrtt-blue.svg)](https://jiangtj.gitlab.io/me)
[![email](https://img.shields.io/badge/email-jiang.taojie@foxmail.com-blue.svg)](mailto:jiang.taojie@foxmail.com)

If you want to get spring-boot's example, you can go to the [official repository](https://github.com/spring-projects/spring-boot)    

There are some simple example for spring-cloud   
![](https://spring.io/img/homepage/diagram-distributed-systems.svg)    
This picture from [Spring'Homepage](https://spring.io/)   

### Version 
- java: jdk 8
- spring boot: 2.1.3
- spring cloud: Greenwich.SR1
- spring boot admin: 2.1.3

### List
- api-gateway
- config-server
- eureka-server
- [admin-server](https://github.com/codecentric/spring-boot-admin): a third-party ui for actuator
- sidecar: a way that non-JVM application taking advantage of Eureka, Ribbon, and Config Server
- hystrix-dashboard
- base-client: based client of eureka
- feign-client: declarative REST client
- config-client: a example loading self-config from config-server

### Sleuth
Need [zipkin server](https://github.com/openzipkin/zipkin)
```bash
curl -sSL https://zipkin.io/quickstart.sh | bash -s
java -jar zipkin.jar
```

