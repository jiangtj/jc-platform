---
title: Spring Cloud 之 Eureka （Finchley版）
date: 2018-7-25
categories: [Java]
tags: [Spring Cloud]
---

在微服务架构中，服务发现是最重要的一环。Spring Cloud提供多个服务注册中心作为选择，如Eureka、Consul、Zookeeper等，当然最常用的是Eureka

# 简介
Eureka由Eureka Server与Eureka Client两部分组成    
Eureka Server是高可用的（可同时作为客户端向其他注册中心注册）服务发现的注册中心，为每个客户端（Eureka Client）提供注册服务，并提供已注册服务信息    
Eureka Client向服务注册中心注册，并提供断路、负载均衡等功能    

<!-- more -->

# 简单的Eureka Server

启动一个Eureka Server，首先需要添加依赖
```xml
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
		</dependency>
```

其次，在启动类Application类中添加Eureka Server相关的注解
```java
@EnableEurekaServer
@SpringBootApplication
public class EurekaServer {
	public static void main(String[] args) {
		SpringApplication.run(EurekaServer.class, args);
	}
}
```

我们启动一个简单的注册中心，需要禁用Eureka Server的客户端行为（这是默认方式，为了保证注册中心的高可用），添加以下的配置
```yml
server:
  port: 8761

eureka:
  instance:
    hostname: localhost
    prefer-ip-address: true
  client:
    registerWithEureka: false
    fetchRegistry: false
    serviceUrl:
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
  server:
    enable-self-preservation: false
```

`enable-self-preservation: false`是关闭自我保护，自我保护是为了保障微服务（实际可用）与Eureka Server之间由于网络因数无法通信时，在一段时间内保留微服务，避免微服务被注销而无法提供服务的情况，由于测试环境实例过少，开启会导致难以测试，一般线上需要开启（默认）    
`registerWithEureka`与`fetchRegistry`，用于禁用客户端行为，Eureka Server作为单个独立应用运行    

# 分布式的Eureka Server
单个Eureka Server，在大部分场景下是足够的，但当企业微服务规模扩大，单个服务难以支撑时，分布式是不可避免的，Eureka Server作为高可用的注册中心，在最初设计时就考虑了相关问题，即它作为服务注册中心的同时，它也能作为客户端，向其他注册中心发起注册    
官方以及许多其他地方的文档基本都是两个对等Eureka Server配置，但事实上，也有需要多个对等的配置。    
多个对等配置如下   
1. 实例一    
```yml
server:
  port: 8761
eureka:
  instance:
    hostname: localhost
  client:
    serviceUrl:
      defaultZone: http://localhost:9001/eureka/,http://localhost:9002/eureka/
  server:
    enable-self-preservation: false
```

2. 实例二  
```yml
server:
  port: 9001
eureka:
  instance:
    hostname: localhost
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/,http://localhost:9002/eureka/
  server:
    enable-self-preservation: false
```

3. 实例三   
```yml
server:
  port: 9002
eureka:
  instance:
    hostname: localhost
  client:
    serviceUrl:
      defaultZone: http://localhost:9001/eureka/,http://localhost:8761/eureka/
  server:
    enable-self-preservation: false
```
这里有一点需要注意，就是将**所有的Eureka Server填写在`defaultZone`中**，并以逗号隔开，自身的地址可填，也可不填，`registerWithEureka`配置了自我注册，默认是开启的。因为服务注册中心同步注册服务，需要彼此之间相互注册，例如A与B相互注册，B与C相互注册，如果一个客户端在A上注册，那么它能在B中发现，而不会在C中发现，如果需要在C中同样发现，那么必须A与C相互注册    

# Eureka Client
客户端是微服务的一个基础，用于向服务注册中心发起注册，当然如果存在多个注册中心的情况，填写一个就好，注册中心之间会相互同步注册的服务信息    
首先需要引入依赖    
```xml
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
```

其次，在启动类Application类中添加Eureka Client相关的注解
```java
@EnableEurekaClient
//或者@EnableDiscoveryClient
//或者@SpringCloudApplication 需要添加断路器依赖（之后会讲这块）
@SpringBootApplication
public class EurekaServer {
	public static void main(String[] args) {
		SpringApplication.run(EurekaServer.class, args);
	}
}
```

最后添加以下的配置，其实Eureka Server包含客户端的内容，这里的配置与上面一致
```yml
server:
  port: 7001
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
```

*默认情况下`defaultZone`的值为`http://localhost:8761/eureka/`*
