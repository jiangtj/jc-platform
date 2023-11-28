package com.jiangtj.platform.auth.reactive.rbac;

import com.jiangtj.platform.auth.context.AuthContext;
import com.jiangtj.platform.auth.reactive.AuthReactorHolder;
import com.jiangtj.platform.auth.reactive.AuthReactorUtils;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.Ordered;
import reactor.core.publisher.Mono;

@Slf4j
public class HasLoginAdvice implements MethodInterceptor, Ordered {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Mono<AuthContext> context = AuthReactorHolder.deferAuthContext()
                .flatMap(ctx -> AuthReactorUtils.hasLoginHandler().apply(ctx));

        return MethodInvocationUtils.handleAdvice(context, invocation);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
