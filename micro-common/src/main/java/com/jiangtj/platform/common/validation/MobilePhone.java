package com.jiangtj.platform.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {
    MobilePhoneStringValidator.class,
    MobilePhoneLongValidator.class
})
@Target({METHOD, FIELD, PARAMETER})
@Retention(RUNTIME)
public @interface MobilePhone {
    String message() default "手机号格式不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
