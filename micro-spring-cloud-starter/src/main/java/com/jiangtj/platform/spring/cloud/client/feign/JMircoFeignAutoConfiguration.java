
package com.jiangtj.platform.spring.cloud.client.feign;


import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnClass(RequestInterceptor.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class JMircoFeignAutoConfiguration {
    @Bean
    public AuthFeignInterceptor authFeignInterceptor() {
        return new AuthFeignInterceptor();
    }
}
