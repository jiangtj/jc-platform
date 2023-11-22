package com.jiangtj.cloud.auth.reactive.rbac;

import com.jiangtj.cloud.auth.annotations.HasTokenType;
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
public class HasTokenTypeAdvice extends AnnotationMethodInterceptorAdvice<HasTokenType> implements Ordered {

    @Override
    public Class<HasTokenType> getAnnotationType() {
        return HasTokenType.class;
    }

    @Override
    public Object invoke(List<HasTokenType> annotations, MethodInvocation invocation) throws Throwable {
        Mono<AuthContext> context = AuthReactorHolder.deferAuthContext();
        for (HasTokenType annotation : annotations) {
            context = context.flatMap(ctx -> AuthReactorUtils.hasRoleHandler(annotation.value()).apply(ctx));
        }
        return MethodInvocationUtils.handleAdvice(context, invocation);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 100;
    }
}
