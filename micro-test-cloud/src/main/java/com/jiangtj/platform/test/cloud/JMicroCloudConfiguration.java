package com.jiangtj.platform.test.cloud;

import org.junit.jupiter.api.Order;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

@Order(Ordered.HIGHEST_PRECEDENCE)
@TestConfiguration(proxyBeanMethods = false)
public class JMicroCloudConfiguration {

    @Bean
    public TestAuthKeyLocator testAuthKeyLocator() {
        return new TestAuthKeyLocator();
    }

    @Bean
    public UserTokenConverter userTokenConverter() {
        return new UserTokenConverter();
    }

}
