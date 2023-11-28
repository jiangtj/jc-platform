package com.jiangtj.platform.auth.servlet.rbac;

import com.jiangtj.platform.auth.servlet.AuthUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;

import java.lang.reflect.Method;

@Slf4j
public class HasLoginAdvice implements MethodBeforeAdvice, Ordered {

    @Override
    public void before(@NonNull Method method, @NonNull Object[] args, Object target) throws Throwable {
        AuthUtils.hasLogin();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
