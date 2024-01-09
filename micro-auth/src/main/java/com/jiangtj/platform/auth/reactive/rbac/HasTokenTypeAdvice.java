package com.jiangtj.platform.auth.reactive.rbac;

import com.jiangtj.platform.auth.annotations.TokenType;
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
public class HasTokenTypeAdvice extends AnnotationMethodInterceptorAdvice<TokenType> implements Ordered {

    @Override
    public Class<TokenType> getAnnotationType() {
        return TokenType.class;
    }

    @Override
    public Object invoke(List<TokenType> annotations, MethodInvocation invocation) throws Throwable {
        Mono<AuthContext> context = AuthReactorHolder.deferAuthContext();
        for (TokenType annotation : annotations) {
            context = context.flatMap(ctx -> AuthReactorUtils.tokenTypeHandler(annotation.value()).apply(ctx));
        }
        return MethodInvocationUtils.handleAdvice(context, invocation);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 100;
    }
}
