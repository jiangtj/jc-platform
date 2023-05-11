package com.jiangtj.cloud.auth.reactive;

import com.jiangtj.cloud.auth.AuthExceptionUtils;
import com.jiangtj.cloud.auth.context.Context;
import com.jiangtj.cloud.auth.context.RoleAuthContext;
import com.jiangtj.cloud.auth.rbac.annotations.HasLogin;
import com.jiangtj.cloud.auth.rbac.annotations.HasPermission;
import com.jiangtj.cloud.auth.rbac.annotations.HasRole;
import com.jiangtj.cloud.auth.rbac.annotations.HasTokenType;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.annotation.AnnotationUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.function.BiFunction;
import java.util.function.Function;

public class AuthReactiveMethodInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Mono<Context> context = AuthReactorHolder.deferAuthContext()
            .flatMap(hasAnnotation(invocation, HasTokenType.class, (anno, ctx) ->
                AuthReactorUtils.tokenTypeHandler(anno.value()).apply(ctx)))
            .flatMap(hasAnnotation(invocation, HasLogin.class, (anno, ctx) ->
                AuthReactorUtils.hasLoginHandler().apply(ctx)))
            .flatMap(inherits -> {
                if (inherits instanceof RoleAuthContext rctx) {
                    return Mono.just(rctx)
                        .flatMap(hasAnnotation(invocation, HasRole.class, (anno, ctx) ->
                            AuthReactorUtils.hasRoleHandler(anno.value()).apply(ctx)))
                        .flatMap(hasAnnotation(invocation, HasPermission.class, (anno, ctx) ->
                            AuthReactorUtils.hasPermissionHandler(anno.value()).apply(ctx)))
                        .thenReturn(inherits);
                } else {
                    return Mono.just(inherits)
                        .flatMap(hasAnnotation(invocation, HasRole.class, (anno, ctx) ->
                            Mono.error(AuthExceptionUtils.noRole("*"))))
                        .flatMap(hasAnnotation(invocation, HasPermission.class, (anno, ctx) ->
                            Mono.error(AuthExceptionUtils.noPermission("*"))));
                }
            });

        Object proceed = invocation.proceed();
        if (proceed instanceof Mono<?> mono) {
            return context.then(mono);
        }
        if (proceed instanceof Flux<?> flux) {
            return context.thenMany(flux);
        }
        return proceed;
    }

    <A extends Annotation, C extends Context> Function<C, Mono<C>> hasAnnotation(MethodInvocation invocation, Class<A> annotationType, BiFunction<A, C, Mono<C>> handler) {
        return context -> {
            Mono<C> result = Mono.just(context);
            Object target = invocation.getThis();
            if (target != null) {
                A classSec = AnnotationUtils.findAnnotation(target.getClass(), annotationType);
                if (classSec != null) {
                    result = result.flatMap(authContext -> handler.apply(classSec, authContext));
                }
            }
            Method method = invocation.getMethod();
            A methodSec = AnnotationUtils.findAnnotation(method, annotationType);
            if (methodSec != null) {
                result = result.flatMap(authContext -> handler.apply(methodSec, authContext));
            }
            return result;
        };
    }
}
