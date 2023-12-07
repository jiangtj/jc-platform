package com.jiangtj.platform.test;

import com.jiangtj.platform.auth.context.AuthContext;

import java.util.List;

public class SimpleTestAuthContextConverter implements TestAnnotationConverter<TestAuthContext> {

    @Override
    public Class<TestAuthContext> getAnnotationClass() {
        return TestAuthContext.class;
    }

    @Override
    public AuthContext convert(TestAuthContext annotation) {
        return new SimpleTestAuthContext(annotation.subject(),
            List.of(annotation.roles()),
            List.of(annotation.permissions()));
    }
}
