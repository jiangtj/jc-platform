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
