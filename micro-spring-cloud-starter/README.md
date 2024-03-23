# Micro Cloud

该模块是一个基于 Spring Cloud 的框架，指在实现微服务间统一的规范，包括权限以及相互间的调用等等，例如在 mirco auth 中，提供了基础的 RBAC 的权限控制，但在微服务光有这个是不够的，在该模块中，提供了基于 JWT 的 Auth Context 解析，使每个微服务都可以达到统一的权限控制

## Server

在该模块中，有一部分内容是规范微服务之间的调用，即微服务主动发起的请求，不由用户触发，所以相对比较简单。在默认情况下，`/actuator/**` 的路径都基于该规范，可通过 `ServerProviderProperties` 进行自定义

### 生成 Token

`AuthServer` 可以非常简单的创建服务间调用所需要的 token（主要是服务间不涉及外部用户）

```java
void create() {
    AuthServer.createServerToken(target);
}
```

### 注解

提供了 `@ServerToken` 注解，如果你的业务代码，也需要微服务之间调用，则可以使用该注解

```java
@ServerToken({"source", "issuer"})
SomeResult getRequest() {
    // do request
}
```

你可以定义哪些微服务可以调用，比如上面需要是 `source` `issuer` 的微服务才能调用

### 测试

提供了 `@WithServer` 注解，用于模拟调用方的请求

```java
@WithServer("issuer")
void doTest() {
    // do test
}
```

也可以通过 `AuthTestServer` 创建 token，和 `AuthServer` 一样的

```java
void doTest() {
    AuthTestServer.createServerTokenFrom("issuer");
}
```

## System

> todo 在考虑要不要将它独立出去，他与业务关联很大，但在一个微服务架构中，提供一个默认的业务规范，也是不错的

同样的系统用户的请求，在各个微服务间，也需要统一的解析

```java
@Bean
public RouterFunction<ServerResponse> roleRoutes(RoleService roleService) {
    return route()
        .filter((request, next) ->
            AuthReactorUtils.hasPermission("needpermission").then(next.handle(request)))
        .GET("/", request -> ServerResponse.ok().bodyValue("ok"))
        .build();
}
```

在 Reactor 应用中，可以使用 `AuthReactorUtils` 控制代码块的权限

```java
@HasRole("role-test-1")
@GetMapping("/role-test-1")
public Mono<String> needRole1(){
    return Mono.just("这个请求需要 role-test-1");
}
```

也可以使用 `@HasRole` 等注解，servlet 应用还在开发中

## 测试(需要引入micro-test-cloud)

```java
@JMicroCloudFluxTest
// @JMicroCloudMvcTest 如果你用的 servlet 应用
class Test {

    @Resource
    WebTestClient client;

    @Test
    @UserToken
    @DisplayName("inject token into webClient")
    void getRole() {
        client.build().get().uri("/")
            .exchange()
            .expectStatus().isOk();
}
```

在测试用例中，可以通过 `@UserToken` 注入 AuthContext 方便测试
