# R2dbc Utils

一些 R2dbc 的工具类

## Page 支持

```java
Page<Persion> page = new PageQueryBuilder<>(template, Persion.class)
  .where(where("name").is("John"))
  .pageable(pageable)
  .apply();
```

## 非Null字段更新

```java
DbUtils.update(template, persion)
```
