package com.jiangtj.cloud.common.aop;

import com.jiangtj.cloud.common.aop.anno.AnnoM;
import com.jiangtj.cloud.common.aop.anno.AnnoT;
import com.jiangtj.cloud.common.aop.anno.AnnoTM;
import org.junit.jupiter.api.Test;
import org.springframework.lang.NonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AnnotationMethodBeforeAdviceTest {

    @Test
    void matchAnnotatedClass() throws NoSuchMethodException {
        Class<AnnotatedClass> clz = AnnotatedClass.class;
        AnnotatedClass target = new AnnotatedClass();
        Method doSome1 = clz.getMethod("doSome1");
        Method doSome2 = clz.getMethod("doSome2");

        TestAnnotationMethodBeforeAdvice<AnnoT> a1 = new TestAnnotationMethodBeforeAdvice<>(AnnoT.class);
        a1.check(doSome1, target).expect(annos -> assertEquals(1, annos.size()));
        a1.check(doSome2, target).expect(annos -> assertEquals(1, annos.size()));

        TestAnnotationMethodBeforeAdvice<AnnoTM> a2 = new TestAnnotationMethodBeforeAdvice<>(AnnoTM.class);
        a2.check(doSome1, target).expect(annos -> assertEquals(1, annos.size()));
        a2.check(doSome2, target).expect(annos -> assertEquals(2, annos.size()));

        TestAnnotationMethodBeforeAdvice<AnnoM> a3 = new TestAnnotationMethodBeforeAdvice<>(AnnoM.class);
        a3.check(doSome1, target).expect(annos -> assertEquals(1, annos.size()));
        a3.check(doSome2, target).expect(annos -> assertEquals(0, annos.size()));
    }

    @Test
    void matchAnnotatedMethod() throws NoSuchMethodException {
        Class<AnnotatedMethod> clz = AnnotatedMethod.class;
        AnnotatedMethod target = new AnnotatedMethod();
        Method doSome1 = clz.getMethod("doSome1");
        Method doSome2 = clz.getMethod("doSome2");

        TestAnnotationMethodBeforeAdvice<AnnoT> a1 = new TestAnnotationMethodBeforeAdvice<>(AnnoT.class);
        a1.check(doSome1, target).expect(annos -> assertEquals(0, annos.size()));
        a1.check(doSome2, target).expect(annos -> assertEquals(0, annos.size()));

        TestAnnotationMethodBeforeAdvice<AnnoTM> a2 = new TestAnnotationMethodBeforeAdvice<>(AnnoTM.class);
        a2.check(doSome1, target).expect(annos -> assertEquals(0, annos.size()));
        a2.check(doSome2, target).expect(annos -> assertEquals(1, annos.size()));

        TestAnnotationMethodBeforeAdvice<AnnoM> a3 = new TestAnnotationMethodBeforeAdvice<>(AnnoM.class);
        a3.check(doSome1, target).expect(annos -> assertEquals(1, annos.size()));
        a3.check(doSome2, target).expect(annos -> assertEquals(0, annos.size()));
    }

    static class TestAnnotationMethodBeforeAdvice<A extends Annotation> extends AnnotationMethodBeforeAdvice<A> {
        private final Class<A> aClass;
        private List<A> annotations;
        public TestAnnotationMethodBeforeAdvice(Class<A> aClass) {
            this.aClass = aClass;
        }
        public TestAnnotationMethodBeforeAdvice<A> check(@NonNull Method method, Object target) {
            try {
                before(method, new Object[]{}, target);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
            return this;
        }
        public void expect(Consumer<List<A>> consumer) {
            consumer.accept(annotations);
        }
        @Override
        public Class<A> getAnnotationType() {
            return aClass;
        }
        @Override
        public void before(@NonNull List<A> annotations, @NonNull Method method, @NonNull Object[] args, Object target) {
            this.annotations = annotations;
        }
    }

}