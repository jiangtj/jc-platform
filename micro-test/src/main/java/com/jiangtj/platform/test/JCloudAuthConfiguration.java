package com.jiangtj.platform.test;

import com.jiangtj.platform.auth.AuthKeyLocator;
import com.jiangtj.platform.auth.cloud.CoreTokenFilter;
import org.junit.jupiter.api.Order;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

@Order(Ordered.HIGHEST_PRECEDENCE)
@TestConfiguration(proxyBeanMethods = false)
public class JCloudAuthConfiguration {

    @Bean
    public AuthKeyLocator authKeyLocator() {
        return new TestAuthKeyLocator();
    }

    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    public CoreTokenFilter coreTokenFilter() {
        return new CoreTokenFilter();
    }

}
