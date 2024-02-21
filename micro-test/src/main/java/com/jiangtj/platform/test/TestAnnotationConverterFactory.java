package com.jiangtj.platform.test;

import com.jiangtj.platform.auth.context.AuthContext;
import com.jiangtj.platform.web.AnnotationUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

@SuppressWarnings({"rawtypes", "unchecked"})
public class TestAnnotationConverterFactory {

    private final ObjectProvider<TestAnnotationConverter> op;

    public TestAnnotationConverterFactory(ObjectProvider<TestAnnotationConverter> op) {
        this.op = op;
    }

    public void setAuthContext(Method method, ApplicationContext context) {
        List<TestAnnotationConverter> list = op.orderedStream().toList();
        for (TestAnnotationConverter converter : list) {
            Optional<? extends Annotation> annotation = AnnotationUtils.findAnnotation(method, converter.getAnnotationClass());
            if (annotation.isPresent()) {
                AuthContext ctx = converter.convert(annotation.get(), context);
                TestAuthContextHolder.setAuthContext(ctx);
                return;
            }
        }
        TestAuthContextHolder.setAuthContext(AuthContext.unauthorized());
    }

}
