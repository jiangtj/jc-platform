package com.jiangtj.platform.common.utils;

import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Optional;
import java.util.stream.Stream;

public interface AnnotationUtils {

    static <A extends Annotation> Optional<A> findAnnotation(AnnotatedElement element, Class<A> annotationType) {
        return MergedAnnotations.from(element, MergedAnnotations.SearchStrategy.INHERITED_ANNOTATIONS)
                .get(annotationType)
                .synthesize(MergedAnnotation::isPresent);
    }

    static <A extends Annotation> Stream<A> streamAnnotations(AnnotatedElement element, Class<A> annotationType) {
        return MergedAnnotations.from(element, MergedAnnotations.SearchStrategy.INHERITED_ANNOTATIONS)
                .stream(annotationType)
                .map(MergedAnnotation::synthesize);
    }

}
