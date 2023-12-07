package com.jiangtj.platform.test;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface TestAuthContext {
    String subject() default "";
    String[] roles() default {};
    String[] permissions() default {};
}
