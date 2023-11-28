package com.jiangtj.platform.auth.reactive;

import com.jiangtj.platform.auth.annotations.HasLogin;
import com.jiangtj.platform.auth.annotations.HasPermission;
import com.jiangtj.platform.auth.annotations.HasRole;
import com.jiangtj.platform.auth.annotations.HasTokenType;
import com.jiangtj.platform.auth.context.AuthContext;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.annotation.AnnotationUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.function.BiFunction;
import java.util.function.Function;

@Deprecated
public class AuthReactiveMethodInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Mono<AuthContext> context = AuthReactorHolder.deferAuthContext()
            .flatMap(hasAnnotation(invocation, HasTokenType.class, (anno, ctx) ->
                AuthReactorUtils.tokenTypeHandler(anno.value()).apply(ctx)))
            .flatMap(hasAnnotation(invocation, HasLogin.class, (anno, ctx) ->
                AuthReactorUtils.hasLoginHandler().apply(ctx)))
            .flatMap(hasAnnotation(invocation, HasRole.class, (anno, ctx) ->
                AuthReactorUtils.hasRoleHandler(anno.value()).apply(ctx)))
            .flatMap(hasAnnotation(invocation, HasPermission.class, (anno, ctx) ->
                AuthReactorUtils.hasPermissionHandler(anno.value()).apply(ctx)));

        Object proceed = invocation.proceed();
        if (proceed instanceof Mono<?> mono) {
            return context.then(mono);
        }
        if (proceed instanceof Flux<?> flux) {
            return context.thenMany(flux);
        }
        return proceed;
    }

    <A extends Annotation> Function<AuthContext, Mono<AuthContext>> hasAnnotation(MethodInvocation invocation, Class<A> annotationType, BiFunction<A, AuthContext, Mono<AuthContext>> handler) {
        return context -> {
            Mono<AuthContext> result = Mono.just(context);
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
