package com.jiangtj.cloud.common.aop;

import com.jiangtj.cloud.common.utils.AnnotationUtils;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.lang.Nullable;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AnnotationMethodInterceptorAdvice<A extends Annotation> implements MethodInterceptor {

    abstract public Class<A> getAnnotationType();

    @Nullable
    abstract public Object invoke(List<A> annotations, MethodInvocation invocation) throws Throwable;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        List<A> list = new ArrayList<>();
        Class<A> annotationType = getAnnotationType();

        Method method = invocation.getMethod();
        Object target = invocation.getThis();

        Target targets = annotationType.getAnnotation(Target.class);
        ElementType[] elementTypes = targets.value();

        for (ElementType type : elementTypes) {
            if (ElementType.TYPE.equals(type)) {
                Optional.ofNullable(target)
                        .map(Object::getClass)
                        .ifPresent(clz -> AnnotationUtils.streamAnnotations(clz, annotationType)
                                .forEach(list::add));
            }
            if (ElementType.METHOD.equals(type)) {
                AnnotationUtils.streamAnnotations(method, annotationType)
                        .forEach(list::add);
            }
        }
        return invoke(list, invocation);
    }

}
