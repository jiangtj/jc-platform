package com.jiangtj.platform.test;

import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

@Order(Ordered.HIGHEST_PRECEDENCE)
@TestConfiguration(proxyBeanMethods = false)
public class JCloudAuthConfiguration {

    @Bean
    public TestAuthContextConverter testAuthContextConverter() {
        return new TestAuthContextConverter();
    }

    @Bean
    public SimpleTestAuthContextConverter simpleTestAuthContextConverter() {
        return new SimpleTestAuthContextConverter();
    }

    @Bean
    @SuppressWarnings("rawtypes")
    public TestAnnotationConverterFactory testAnnotationConverterFactory(ObjectProvider<TestAnnotationConverter> converters) {
        return new TestAnnotationConverterFactory(converters);
    }

}
