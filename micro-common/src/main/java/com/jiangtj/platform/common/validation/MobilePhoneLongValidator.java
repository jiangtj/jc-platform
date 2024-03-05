package com.jiangtj.platform.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MobilePhoneLongValidator implements ConstraintValidator<MobilePhone, Long> {

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value >= 10000000000L && value < 20000000000L;
    }
}
