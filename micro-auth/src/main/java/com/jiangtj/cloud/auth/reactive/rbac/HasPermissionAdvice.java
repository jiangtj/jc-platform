package com.jiangtj.cloud.auth.reactive.rbac;

import com.jiangtj.cloud.auth.annotations.HasPermission;
import com.jiangtj.cloud.auth.context.AuthContext;
import com.jiangtj.cloud.auth.reactive.AuthReactorHolder;
import com.jiangtj.cloud.auth.reactive.AuthReactorUtils;
import com.jiangtj.cloud.common.aop.AnnotationMethodInterceptorAdvice;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.Ordered;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
public class HasPermissionAdvice extends AnnotationMethodInterceptorAdvice<HasPermission> implements Ordered {

    @Override
    public Class<HasPermission> getAnnotationType() {
        return HasPermission.class;
    }

    @Override
    public Object invoke(List<HasPermission> annotations, MethodInvocation invocation) throws Throwable {
        Mono<AuthContext> context = AuthReactorHolder.deferAuthContext();
        for (HasPermission annotation : annotations) {
            context = context.flatMap(ctx -> AuthReactorUtils.hasPermissionHandler(annotation.value()).apply(ctx));
        }
        return MethodInvocationUtils.handleAdvice(context, invocation);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 100;
    }
}
