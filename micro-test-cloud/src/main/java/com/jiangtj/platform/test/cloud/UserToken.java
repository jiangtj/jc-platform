package com.jiangtj.platform.test.cloud;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface UserToken {
    long id() default 1;
    String[] role() default {};
}
