package com.jiangtj.cloud.common.aop;

import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractGenericPointcutAdvisor;
import org.springframework.lang.NonNull;

import java.lang.annotation.Annotation;

public class AnnotationMethodBeforeAdvisor<A extends Annotation> extends AbstractGenericPointcutAdvisor {

    private final AnnotationPointcut<A> pointcut;

    public AnnotationMethodBeforeAdvisor(AnnotationMethodBeforeAdvice<A> advice) {
        setAdvice(advice);
        pointcut = new AnnotationPointcut<>(advice.getAnnotationType());
    }

    @NonNull
    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }
}
