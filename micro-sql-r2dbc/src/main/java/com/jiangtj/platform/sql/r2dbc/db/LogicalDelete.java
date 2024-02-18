package com.jiangtj.platform.sql.r2dbc.db;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LogicalDelete {
    String value() default "is_deleted";
    int set() default 1;
}
