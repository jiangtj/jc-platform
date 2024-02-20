package com.jiangtj.platform.web.aop;

import com.jiangtj.platform.web.aop.anno.AnnoM;
import com.jiangtj.platform.web.aop.anno.AnnoT;
import com.jiangtj.platform.web.aop.anno.AnnoTM;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnnotationPointcutTest {

    @Test
    void matchAnnotatedClass() throws NoSuchMethodException {
        Class<AnnotatedClass> annotatedClass = AnnotatedClass.class;
        Method doSome1 = annotatedClass.getMethod("doSome1");
        Method doSome2 = annotatedClass.getMethod("doSome2");

        AnnotationPointcut<AnnoT> p1 = new AnnotationPointcut<>(AnnoT.class);
        assertTrue(p1.matches(doSome1, annotatedClass));
        assertTrue(p1.matches(doSome2, annotatedClass));

        AnnotationPointcut<AnnoTM> p2 = new AnnotationPointcut<>(AnnoTM.class);
        assertTrue(p2.matches(doSome1, annotatedClass));
        assertTrue(p2.matches(doSome2, annotatedClass));

        AnnotationPointcut<AnnoM> p3 = new AnnotationPointcut<>(AnnoM.class);
        assertTrue(p3.matches(doSome1, annotatedClass));
        assertFalse(p3.matches(doSome2, annotatedClass));
    }

    @Test
    void matchAnnotatedMethod() throws NoSuchMethodException {
        Class<AnnotatedMethod> annotatedMethod = AnnotatedMethod.class;
        Method doSome1 = annotatedMethod.getMethod("doSome1");
        Method doSome2 = annotatedMethod.getMethod("doSome2");

        AnnotationPointcut<AnnoT> p1 = new AnnotationPointcut<>(AnnoT.class);
        assertFalse(p1.matches(doSome1, annotatedMethod));
        assertFalse(p1.matches(doSome2, annotatedMethod));

        AnnotationPointcut<AnnoTM> p2 = new AnnotationPointcut<>(AnnoTM.class);
        assertFalse(p2.matches(doSome1, annotatedMethod));
        assertTrue(p2.matches(doSome2, annotatedMethod));

        AnnotationPointcut<AnnoM> p3 = new AnnotationPointcut<>(AnnoM.class);
        assertTrue(p3.matches(doSome1, annotatedMethod));
        assertFalse(p3.matches(doSome2, annotatedMethod));
    }
}