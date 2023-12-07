package com.jiangtj.platform.test;

import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class JCloudExtension implements BeforeTestExecutionCallback {

    @Override
    public void beforeTestExecution(ExtensionContext extensionContext) throws Exception {
        ApplicationContext applicationContext = SpringExtension.getApplicationContext(extensionContext);
        TestAnnotationConverterFactory factory = applicationContext.getBean(TestAnnotationConverterFactory.class);
        factory.setAuthContext(extensionContext.getRequiredTestMethod());
    }

}
