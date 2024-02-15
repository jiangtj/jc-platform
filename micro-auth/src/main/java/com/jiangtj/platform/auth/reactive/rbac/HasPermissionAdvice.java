package com.jiangtj.platform.auth.reactive.rbac;

import com.jiangtj.platform.auth.annotations.HasPermission;
import com.jiangtj.platform.auth.context.AuthContext;
import com.jiangtj.platform.auth.reactive.AuthReactorHolder;
import com.jiangtj.platform.auth.reactive.AuthReactorUtils;
import com.jiangtj.platform.common.aop.ReactiveAnnotationMethodBeforeAdvice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
public class HasPermissionAdvice extends ReactiveAnnotationMethodBeforeAdvice<HasPermission> implements Ordered {

    @Override
    public Class<HasPermission> getAnnotationType() {
        return HasPermission.class;
    }

    @Override
    public Mono<Void> before(List<HasPermission> annotations, Object[] args) {
        Mono<AuthContext> context = AuthReactorHolder.deferAuthContext();
        for (HasPermission annotation : annotations) {
            context = context.flatMap(ctx -> AuthReactorUtils.hasPermissionHandler(annotation.value()).apply(ctx));
        }
        return context.then();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 100;
    }
}
