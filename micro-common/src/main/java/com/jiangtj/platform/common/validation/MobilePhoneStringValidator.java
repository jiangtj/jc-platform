package com.jiangtj.platform.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class MobilePhoneStringValidator implements ConstraintValidator<MobilePhone, String> {

    static Pattern p = Pattern.compile("^[1][0-9]{10}$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        String defaultConstraintMessageTemplate = context.getDefaultConstraintMessageTemplate();
        return p.matcher(value).matches();
    }
}
