package com.jiangtj.platform.auth.annotations;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.function.Consumer;

@Deprecated
public interface RBACAnnotationUtils {

    static boolean hasAnnotations(@NonNull Method method, @NonNull Class<?> targetClass) {
        if (hasAnnotations(method, targetClass, HasLogin.class)) {
            return true;
        }
        if (hasAnnotations(method, targetClass, HasRole.class)) {
            return true;
        }
        if (hasAnnotations(method, targetClass, HasPermission.class)) {
            return true;
        }
        return hasAnnotations(method, targetClass, HasTokenType.class);
    }

    static <A extends Annotation> boolean hasAnnotations(@NonNull Method method, @NonNull Class<?> targetClass, Class<A> annotationType) {
        Annotation methodSec = AnnotationUtils.findAnnotation(method, annotationType);
        if (methodSec != null) {
            return true;
        }
        Annotation clsSec = AnnotationUtils.findAnnotation(targetClass, annotationType);
        return clsSec != null;
    }

    static <A extends Annotation> void ifPresent(Object target, @NonNull Method method, Class<A> annotationType, Consumer<A> handler) {
        if (target != null) {
            A classSec = AnnotationUtils.findAnnotation(target.getClass(), annotationType);
            if (classSec != null) {
                handler.accept(classSec);
            }
        }
        A methodSec = AnnotationUtils.findAnnotation(method, annotationType);
        if (methodSec != null) {
            handler.accept(methodSec);
        }
    }

}
