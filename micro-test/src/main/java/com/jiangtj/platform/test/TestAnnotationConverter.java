package com.jiangtj.platform.test;

import com.jiangtj.platform.auth.context.AuthContext;
import org.springframework.context.ApplicationContext;

import java.lang.annotation.Annotation;

public interface TestAnnotationConverter<A extends Annotation> {

    Class<A> getAnnotationClass();

    AuthContext convert(A annotation, ApplicationContext context);

}
