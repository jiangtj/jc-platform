package com.jiangtj.platform.common.aop;

import com.jiangtj.platform.common.utils.AnnotationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.StaticMethodMatcherPointcut;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

@Slf4j
public class AnnotationPointcut<A extends Annotation> extends StaticMethodMatcherPointcut {

    private final Class<A> annotationType;

    public AnnotationPointcut(Class<A> annotationType) {
        this.annotationType = annotationType;
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        Target targets = annotationType.getAnnotation(Target.class);
        ElementType[] elementTypes = targets.value();

        for (ElementType type : elementTypes) {
            if (ElementType.TYPE.equals(type)) {
                if (AnnotationUtils.findAnnotation(targetClass, annotationType).isPresent()) {
                    return true;
                }
            }
            if (ElementType.METHOD.equals(type)) {
                if (AnnotationUtils.findAnnotation(method, annotationType).isPresent()) {
                    return true;
                }
            }
        }
        return false;
    }
}
