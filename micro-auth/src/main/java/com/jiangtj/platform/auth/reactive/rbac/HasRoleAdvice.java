package com.jiangtj.platform.auth.reactive.rbac;

import com.jiangtj.platform.auth.annotations.HasRole;
import com.jiangtj.platform.auth.context.AuthContext;
import com.jiangtj.platform.auth.reactive.AuthReactorHolder;
import com.jiangtj.platform.auth.reactive.AuthReactorUtils;
import com.jiangtj.platform.common.aop.AnnotationMethodInterceptorAdvice;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.Ordered;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
public class HasRoleAdvice extends AnnotationMethodInterceptorAdvice<HasRole> implements Ordered {

    @Override
    public Class<HasRole> getAnnotationType() {
        return HasRole.class;
    }

    @Override
    public Object invoke(List<HasRole> annotations, MethodInvocation invocation) throws Throwable {
        Mono<AuthContext> context = AuthReactorHolder.deferAuthContext();
        for (HasRole annotation : annotations) {
            context = context.flatMap(ctx -> AuthReactorUtils.hasRoleHandler(annotation.value()).apply(ctx));
        }
        return MethodInvocationUtils.handleAdvice(context, invocation);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 100;
    }
}
