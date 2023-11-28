package com.jiangtj.platform.auth.servlet.rbac;

import com.jiangtj.platform.auth.annotations.HasTokenType;
import com.jiangtj.platform.auth.servlet.AuthUtils;
import com.jiangtj.platform.common.aop.AnnotationMethodBeforeAdvice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;

import java.lang.reflect.Method;
import java.util.List;

@Slf4j
public class HasTokenTypeAdvice extends AnnotationMethodBeforeAdvice<HasTokenType> implements Ordered {

    @Override
    public Class<HasTokenType> getAnnotationType() {
        return HasTokenType.class;
    }

    @Override
    public void before(List<HasTokenType> annotations, Method method, Object[] args, Object target) {
        for (HasTokenType annotation : annotations) {
            AuthUtils.isTokenType(annotation.value());
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 100;
    }
}
