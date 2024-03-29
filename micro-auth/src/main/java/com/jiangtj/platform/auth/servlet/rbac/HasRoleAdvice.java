package com.jiangtj.platform.auth.servlet.rbac;

import com.jiangtj.platform.auth.annotations.HasRole;
import com.jiangtj.platform.auth.servlet.AuthUtils;
import com.jiangtj.platform.web.aop.AnnotationMethodBeforeAdvice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;

import java.lang.reflect.Method;
import java.util.List;

@Slf4j
public class HasRoleAdvice extends AnnotationMethodBeforeAdvice<HasRole> implements Ordered {

    @Override
    public Class<HasRole> getAnnotationType() {
        return HasRole.class;
    }

    @Override
    public void before(List<HasRole> annotations, Method method, Object[] args, Object target) {
        for (HasRole annotation : annotations) {
            AuthUtils.hasRole(annotation.value());
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 100;
    }
}
