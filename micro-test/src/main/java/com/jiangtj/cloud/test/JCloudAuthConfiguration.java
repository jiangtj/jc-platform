package com.jiangtj.cloud.test;

import com.jiangtj.cloud.auth.AuthKeyLocator;
import org.junit.jupiter.api.Order;
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

}
