# Micro Auth

对于 Spring Boot 应用，提供基础的鉴权环境

## 基础用法

下面是一个例子，使用 jwt 解析 bearer token

```java
public class JsonAuthContextConverter implements AuthContextConverter {

    @Resource
    private RoleProvider roleProvider; // 你可以将获取权限的 service 继承 RoleProvider

    @Override
    public AuthContext convert(HttpRequest request) {
        List<String> headers = request.getHeaders().get(AuthRequestAttributes.TOKEN_HEADER_NAME);
        if (headers == null || headers.size() != 1) {
            return AuthContext.unLogin();
        }

        String token = headers.get(0);
        JwtParser parser = Jwts.parser()
            .verifyWith(key)
            .build();
        Claims body = parser.parseSignedClaims(token).getPayload();
        return RoleProviderAuthContext.create(body.subject(), roleProvider, body.get("roles", List.class));
    }

}
```

## 测试(需要引入micro-test)

### 基础用法

```java
@JMicroTest
@AutoConfigureWebTestClient
class Test {

    @Resource
    WebTestClient client;

    @Test
    @WithMockUser(subject="?", roles = {"?","?"})
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
    public AuthContext convert(UserToken annotation, ApplicationContext context) {
        long id = annotation.id();
        List<String> roles = Stream.of(annotation.role())
            .map(KeyUtils::toKey)
            .toList();
        String test = authServer.createUserToken(String.valueOf(id), roles, "test");
        return factory.getAuthContext(test);
    }
}
```
