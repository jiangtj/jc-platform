---
title: Spring Cloud 之 Feign （Finchley版）
date: 2018-8-1
categories: [Java]
tags: [Spring Cloud]
---

Feign是轻量级、声明式的Http请求客户端，它吸收了来自的Retrofit JAXRS-2.0和WebSocket的灵感，为了使写Http请求变得更容易而诞生    

Feign一开始作为Eureka的子项目，用于简化Http请求。但由于其不断完善，目前作为一个轻量级、声明式的Http请求客户端项目，独立维护。在Spring Cloud中，其引入了Feign，并提供了一系列默认的配置与Spring MVC注解的支持。因此，Feign一直被作为首先的Http请求客户端。   

<!-- more -->

# 准备工作

需要Eureka Server，这部分看之前的文档部署，这篇以及以后的文章不在多提

搭建基础的Eureka Client，给Feign Clinet调用    

## 引入web相关依赖   

```xml
        <!-- 为了方便引入了lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-webflux</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>io.projectreactor</groupId>
            <artifactId>reactor-test</artifactId>
            <scope>test</scope>
        </dependency>
```
## 编写Curl Controller   

```java
@Slf4j
@RestController
public class CurlController {

    private static ObjectMapper objectMapper;

    @GetMapping("/curl/{id}")
    public Mono<String> get(@PathVariable Long id){
        return Mono.just(id).map(item -> "Your id is " + item);
    }

    @GetMapping("/curl")
    public Mono<String> get(@RequestParam MultiValueMap<String,String> queryParams){
        return Mono.just(queryParams).map(item -> "Your QueryParams is " + item);
    }

    @PostMapping("/curl")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<String> post(@RequestBody Mono<User> user){
        return user.map(CurlController::toJson).map(item -> "Your Body is " + item);
    }

    @PutMapping("/curl")
    public Mono<String> put(@RequestBody Mono<User> user){
        return post(user);
    }

    @DeleteMapping("/curl")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete() {
        throw new RuntimeException("Fail");
    }

    private static ObjectMapper getObjectMapper(){
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        return objectMapper;
    }
    private static String toJson(Object object) {
        ObjectMapper mapper = getObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
```
## 编写测试用例   

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class CurlControllerTest {

    @Autowired
    private ApplicationContext context;

    private WebTestClient client;

    @Before
    public void setUp() {
        client = WebTestClient.bindToApplicationContext(context).build();
    }

    @Test
    public void get() {

        client.get().uri("/curl/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Your id is 1");

        client.get().uri("/curl?name=Jone Test")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Your QueryParams is {name=[Jone Test]}");

    }

    @Test
    public void post() {
        client.post().uri("/curl")
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(User.of("Jone Po",25,1))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(String.class).isEqualTo("Your Body is {\"name\":\"Jone Po\",\"age\":25,\"sex\":1}");
    }

    @Test
    public void put() {
        client.put().uri("/curl")
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(User.of("Jone Po",25,1))
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Your Body is {\"name\":\"Jone Po\",\"age\":25,\"sex\":1}");
    }

    @Test
    public void delete() {
        client.delete().uri("/curl").exchange().expectStatus().isNoContent();
    }
}
```
## 测试接口   

```
[ERROR] Tests run: 4, Failures: 1, Errors: 0, Skipped: 0, Time elapsed: 0.204 s <<< FAILURE! - in com.jtj.cloud.baseclient.CurlControllerTest
[ERROR] delete(com.jtj.cloud.baseclient.CurlControllerTest)  Time elapsed: 0.077 s  <<< FAILURE!
```
测试接口如预期，成功3个，失败1个（删除接口）    

# Feign服务

## 相对于基础的客户端，多引入Feign依赖，并启用Feign    

```xml
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-openfeign</artifactId>
		</dependency>
```
```java
@EnableFeignClients
@SpringCloudApplication
public class FeignClientApplication {
    //...
}
```
## 编写声明式的Feign接口   

```java
@FeignClient(value = "base-client")
public interface BaseClient {

    @GetMapping("/")
    String getBaseClientData();

    @GetMapping("/curl/{id}")
    String getUser(@PathVariable("id") Long id);

    @GetMapping("/curl")
    String getUser(@RequestParam Map<String,String> query);

    @PostMapping("/curl")
    String postUser(@RequestBody User user);

    @PutMapping("/curl")
    String putUser(@RequestBody User user);

    @DeleteMapping("/curl")
    void deleteUser();

}
```
该接口注解与Spring MVC的基本一致（@PathVariable不能省略value值）   
其中@FeignClient定义该接口实例化为Feign服务并注入到Spring中，由Spring管理，value值为配置文件中微服务的名称（spring.application.name的值）   
## 调用Feign服务   

```java

	@Resource
	private BaseClient baseClient;

    
	@GetMapping("/base/curl")
	public Map<String,String> getBaseClientCurl(){
		Map<String,String> result = new HashMap<>();

		result.put("ID 1",baseClient.getUser(1L));
		Map<String,String> query = new HashMap<>();

		query.put("name","Jone Taki");
		result.put("Query Jone",baseClient.getUser(query));

		result.put("Post",baseClient.postUser(User.of("Jone Tiki",20,1)));
		result.put("Put",baseClient.postUser(User.of("Jone Kolo",30,1)));

		try {
			baseClient.deleteUser();
			result.put("Delete","success");
		} catch (RuntimeException e) {
			result.put("Delete","fail: " + e.getMessage());
		}

		return result;
	}

```

## 访问接口测试   

我们能得到如下结果，其中Delete是失败的
```json
{
    "Delete":"fail: BaseClient#deleteUser() failed and no fallback available.",
    "Query Jone":"Your QueryParams is {name=[Jone Taki]}",
    "Post":"Your Body is {\"name\":\"Jone Tiki\",\"age\":20,\"sex\":1}",
    "ID 1":"Your id is 1",
    "Put":"Your Body is {\"name\":\"Jone Kolo\",\"age\":30,\"sex\":1}"
}
```

上述这些例子包含了基本的REST操作，也就是Feign的基本使用

# 参考
- [Feign源码地址](https://github.com/OpenFeign/feign)
- [Spring Cloud OpenFeign 官方文档](http://cloud.spring.io/spring-cloud-openfeign/single/spring-cloud-openfeign.html)
- [例子源码地址（可能存在改动和完善）](https://github.com/JiangTJ/spring-cloud-examples/tree/master/simple-example)


