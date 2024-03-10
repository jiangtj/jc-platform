# Common

一些通用的工具类

## DistinctWrapper

受 jdk22 Stream Gatherers 启发, stream 的 distinct 需要覆盖 equals 与 hashCode, 虽然 jdk22 有更好的写法, 但目前对于
jdk17+ 可能提供一个 Wrapper 类更合适

```java
List<User> users = Arrays.asList(
    new User(1, "n1"),
    new User(2, "n2"),
    new User(1, "n3"));

long count1 = users.stream().distinct().count();

assertEquals(3,count1);

long count2 = users.stream()
    .map(user -> new DistinctWrapper<>(user, User::id))
    .distinct()
    .map(DistinctWrapper::value)
    .count();

assertEquals(2,count2);
```
