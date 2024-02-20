package com.jiangtj.platform.spring.boot.aop;

import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractGenericPointcutAdvisor;

import java.lang.annotation.Annotation;

public class AnnotationMethodBeforeAdvisor<A extends Annotation> extends AbstractGenericPointcutAdvisor {

    private final AnnotationPointcut<A> pointcut;

    public AnnotationMethodBeforeAdvisor(AnnotationMethodBeforeAdvice<A> advice) {
        setAdvice(advice);
        pointcut = new AnnotationPointcut<>(advice.getAnnotationType());
    }

    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }
}
