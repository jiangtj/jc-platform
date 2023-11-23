package com.jiangtj.cloud.common.aop;

import com.jiangtj.cloud.common.utils.AnnotationUtils;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.lang.Nullable;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AnnotationMethodBeforeAdvice<A extends Annotation> implements MethodBeforeAdvice {

    abstract public Class<A> getAnnotationType();

    abstract public void before(List<A> annotations, Method method, Object[] args,  @Nullable Object target);

    @Override
    public void before(Method method, Object[] args, @Nullable Object target) throws Throwable {
        List<A> list = new ArrayList<>();
        Class<A> annotationType = getAnnotationType();

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

        before(list, method, args, target);
    }

}
