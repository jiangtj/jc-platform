package com.jiangtj.cloud.test;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface UserToken {
    long id() default 1;
    String[] role() default {};
}
