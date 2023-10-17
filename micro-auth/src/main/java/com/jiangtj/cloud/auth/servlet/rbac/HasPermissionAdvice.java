package com.jiangtj.cloud.auth.servlet.rbac;

import com.jiangtj.cloud.auth.rbac.annotations.HasPermission;
import com.jiangtj.cloud.common.aop.AnnotationMethodBeforeAdvice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

import java.lang.reflect.Method;
import java.util.List;

@Slf4j
public class HasPermissionAdvice extends AnnotationMethodBeforeAdvice<HasPermission> {

    @Override
    public Class<HasPermission> getAnnotationType() {
        return HasPermission.class;
    }

    @Override
    public void before(@NonNull List<HasPermission> annotations, @NonNull Method method, @NonNull Object[] args, Object target) {
    }
}
