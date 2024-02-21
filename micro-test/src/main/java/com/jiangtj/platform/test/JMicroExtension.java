package com.jiangtj.platform.test;

import com.jiangtj.platform.web.AnnotationUtils;
import org.junit.jupiter.api.extension.*;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.Method;

public class JMicroExtension implements BeforeEachCallback,
    BeforeTestExecutionCallback,
    AfterTestExecutionCallback,
    AfterEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
    }

    @Override
    public void beforeTestExecution(ExtensionContext extensionContext) throws Exception {
        ApplicationContext applicationContext = SpringExtension.getApplicationContext(extensionContext);
        TestAnnotationConverterFactory factory = applicationContext.getBean(TestAnnotationConverterFactory.class);
        Method method = extensionContext.getRequiredTestMethod();

        Class<?> testClass = extensionContext.getRequiredTestClass();
        AnnotationUtils.findAnnotation(testClass, WithMockRoleProvider.class)
            .ifPresent(annotation -> {
                applicationContext.getBeanProvider(annotation.value())
                    .ifAvailable(TestAuthContextHolder::setProvider);
            });
        AnnotationUtils.findAnnotation(method, WithMockRoleProvider.class)
            .ifPresent(annotation -> {
                applicationContext.getBeanProvider(annotation.value())
                    .ifAvailable(TestAuthContextHolder::setProvider);
            });

        factory.setAuthContext(method, applicationContext);
    }

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
        TestAuthContextHolder.clearAuthContext();
        TestAuthContextHolder.clearProvider();
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
    }
}
