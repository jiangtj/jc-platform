# Micro JOOQ

提供业务层面的JOOQ封装

## 使用

```xml
<dependency>
    <groupId>com.jiangtj.platform</groupId>
    <artifactId>micro-sql-jooq</artifactId>
    <version>${last-version}</version>
</dependency>
```

## PageUtils

分页查询

### fluent api

```java
PageUtils.selectFrom(create, ADMIN_USER)
    .conditions(condition(new AdminUserRecord(user)))
    .pageable(pageable)
    .fetchPage(AdminUser.class)
```

如果你希望修改查询内容

```java
PageUtils.select(create, field("val1"), field("val2") ...)
    .from(ADMIN_USER)
    .conditions(condition(new AdminUserRecord(user)))
    .pageable(pageable)
    .fetchPage(AdminUser.class)
```

### 分别查询列表与总数
```java
Condition condition = ...;
PageUtils.selectLimitList(create.select(table).from(table), pageable, condition);
PageUtils.selectCount(create.selectCount().from(table), condition);
```

### 响应式

首先你需要先添加一个DSLContext配置

```java
@Bean
public DSLContext dslContext(ConnectionFactory connectionFactory) {
    return DSL.using(connectionFactory);
}
```

之后，使用 `biSubscribe()` 获取并转换值，下面是一个例子

```java
PageUtils.selectFrom(create, SYSTEM_USER)
    .conditions(SYSTEM_USER.IS_DELETED.eq((byte) 0)
        .and(StringUtils.hasLength(user.getUsername()) ?
            SYSTEM_USER.USERNAME.like(user.getUsername() + "%") :
            noCondition()))
    .pageable(pageable)
    .biSubscribe((listS, countS) -> Mono.zip(
        Flux.from(listS).map(l -> l.into(SystemUser.class)).collectList(),
        Mono.from(countS).map(Record1::value1)))
    .map(tuple -> new PageImpl<>(tuple.getT1(), pageable, tuple.getT2()))
```

## GenerateHelper

帮助生成JOOQ代码的工具类，在程序中控制JOOQ代码生成比maven插件有更多的自定义空间，尤其是当项目中已经有了数据库链接配置，那么我们需要做的只是读取配置，转换成我们需要的类，这个工具类就是做这些杂事的

```java
@SpringBootTest
public class GenerateTest {

    @Resource
    DataSourceProperties properties;

    @Test
    public void generate() throws Exception {
        GenerateHelper.init(properties);
        GenerationTool.generate(new Configuration()
            .withJdbc(GenerateHelper.getJdbc())
            .withGenerator(new Generator()
                    .withDatabase(GenerateHelper.getDatabase(".*"))
                    .withTarget(GenerateHelper.getTarget("com.jiangtj.platform.system.jooq"))
                    .withGenerate(new Generate()
                            .withPojos(true)
                            .withPojosAsJavaRecordClasses(true)
                            .withValidationAnnotations(true)
                            .withDaos(true))));
    }

}
```

- GenerateHelper.init(properties) 添加 spring boot 的数据源配置
- GenerateHelper.getJdbc() 获取 jdbc 配置
- GenerateHelper.getDatabase(tableNamePattern) 获取数据库配置
- GenerateHelper.getTarget(packageName) 获取生成位置的配置
