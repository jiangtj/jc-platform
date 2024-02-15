package com.jiangtj.platform.common.aop;

import org.springframework.lang.Nullable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.annotation.Annotation;
import java.util.List;

public abstract class ReactiveAnnotationMethodBeforeAdvice<A extends Annotation> extends ReactiveAnnotationMethodInterceptorAdvice<A> {

    abstract public Mono<Void> before(List<A> annotations, Object[] args);

    @Nullable
    public Mono<?> invoke(List<A> annotations, Mono<?> proceed, Object[] args) throws Throwable{
        return before(annotations, args).then(proceed);
    }

    @Nullable
    public Flux<?> invoke(List<A> annotations, Flux<?> proceed, Object[] args) throws Throwable{
        return before(annotations, args).thenMany(proceed);
    }

}
