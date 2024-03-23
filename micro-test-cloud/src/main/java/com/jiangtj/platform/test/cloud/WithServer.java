package com.jiangtj.platform.test.cloud;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface WithServer {
    String value() default "any"; // issuer
}
