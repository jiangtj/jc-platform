# Micro Auth

对于 Spring Boot 应用，提供基础的鉴权环境

## 基础用法

开发中...

## 测试(需要引入micro-test)

### 基础用法

```java
@JMicroTest
@AutoConfigureWebTestClient
class Test {

    @Resource
    WebTestClient client;

    @Test
    @TestAuthContext(subject="?", roles = {"?","?"})
    void getRole() {
        client.build().get().uri("/")
            .exchange()
            .expectStatus().isOk();
    }
```

### 自定义注解

下面是一个@UserToken自定义例子

```java
public class UserTokenConverter implements TestAnnotationConverter<UserToken> {

    @Resource
    private JwtAuthContextFactory factory;
    @Resource
    private AuthServer authServer;

    @Override
    public Class<UserToken> getAnnotationClass() {
        return UserToken.class;
    }

    @Override
    public AuthContext convert(UserToken annotation) {
        long id = annotation.id();
        List<String> roles = Stream.of(annotation.role())
            .map(KeyUtils::toKey)
            .toList();
        String test = authServer.createUserToken(String.valueOf(id), roles, "test");
        return factory.getAuthContext(test);
    }
}
```
