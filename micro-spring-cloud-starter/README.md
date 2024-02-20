# Micro Auth Cloud

## 基础用法

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
