package com.jiangtj.platform.auth.servlet.rbac;

import com.jiangtj.platform.auth.annotations.HasPermission;
import com.jiangtj.platform.auth.servlet.AuthUtils;
import com.jiangtj.platform.spring.boot.aop.AnnotationMethodBeforeAdvice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;

import java.lang.reflect.Method;
import java.util.List;

@Slf4j
public class HasPermissionAdvice extends AnnotationMethodBeforeAdvice<HasPermission> implements Ordered {

    @Override
    public Class<HasPermission> getAnnotationType() {
        return HasPermission.class;
    }

    @Override
    public void before(@NonNull List<HasPermission> annotations, @NonNull Method method, @NonNull Object[] args, Object target) {
        for (HasPermission annotation : annotations) {
            AuthUtils.hasPermission(annotation.value());
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 100;
    }
}
