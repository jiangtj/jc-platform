package com.jiangtj.platform.spring.boot.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.lang.Nullable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.annotation.Annotation;
import java.util.List;

public abstract class ReactiveAnnotationMethodInterceptorAdvice<A extends Annotation> implements MethodInterceptor {

    abstract public Class<A> getAnnotationType();

    @Nullable
    abstract public Mono<?> invoke(List<A> annotations, Mono<?> proceed, Object[] args) throws Throwable;

    @Nullable
    abstract public Flux<?> invoke(List<A> annotations, Flux<?> proceed, Object[] args) throws Throwable;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object proceed = invocation.proceed();

        if (proceed instanceof Mono<?> mono) {
            List<A> list = AopAnnotationUtils.findAnnotation(invocation, getAnnotationType());
            return invoke(list, mono, invocation.getArguments());
        }

        if (proceed instanceof Flux<?> flux) {
            List<A> list = AopAnnotationUtils.findAnnotation(invocation, getAnnotationType());
            return invoke(list, flux, invocation.getArguments());
        }

        // 响应式不拦截非响应式的接口
        return proceed;
    }

}
