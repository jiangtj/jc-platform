package com.jiangtj.platform.test;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface WithMockUser {
    String subject() default "";
    String[] roles() default {};
    String[] permissions() default {};
    boolean inheritRoleProvider() default true;
}
