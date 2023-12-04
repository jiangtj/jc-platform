package com.jiangtj.platform.auth.cloud;


import com.jiangtj.platform.auth.AuthKeyLocator;
import com.jiangtj.platform.auth.reactive.AuthWebClientFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class ReactiveCloudAutoConfiguration {

    @Bean
    @LoadBalanced
    @ConditionalOnProperty(prefix="auth", name = "init-load-balanced-client", havingValue = "true", matchIfMissing = true)
    WebClient.Builder loadBalanced(AuthWebClientFilter filter) {
        return WebClient.builder().filter(filter);
    }

    @Bean
    @ConditionalOnMissingBean
    AuthKeyLocator authKeyLocator() {
        return new ReactiveAuthKeyLocator();
    }

    @Bean
    @ConditionalOnMissingBean(CoreTokenFilter.class)
    public ReactivePublicKeyFilter reactivePublicKeyFilter() {
        return new ReactivePublicKeyFilter();
    }

}
