
package com.jiangtj.platform.auth.cloud;


import com.jiangtj.platform.auth.AuthKeyLocator;
import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ServletCloudAutoConfiguration {

    @Bean
    @LoadBalanced
    @ConditionalOnProperty(prefix="auth", name = "init-load-balanced-client", havingValue = "true", matchIfMissing = true)
    RestTemplate loadBalanced() {
        return new RestTemplate();
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthKeyLocator authKeyLocator() {
        return new ServletAuthKeyLocator();
    }

    @Bean
    @ConditionalOnClass(RequestInterceptor.class)
    @ConditionalOnProperty(prefix="auth", name = "init-load-balanced-client", havingValue = "true", matchIfMissing = true)
    public AuthFeignInterceptor authFeignInterceptor() {
        return new AuthFeignInterceptor();
    }
}
