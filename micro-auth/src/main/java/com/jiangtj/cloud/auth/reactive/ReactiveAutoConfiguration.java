package com.jiangtj.cloud.auth.reactive;


import com.jiangtj.cloud.auth.AuthKeyLocator;
import com.jiangtj.cloud.auth.AuthProperties;
import com.jiangtj.cloud.auth.rbac.annotations.HasLogin;
import com.jiangtj.cloud.auth.rbac.annotations.HasPermission;
import com.jiangtj.cloud.auth.rbac.annotations.HasRole;
import com.jiangtj.cloud.auth.rbac.annotations.HasTokenType;
import com.jiangtj.cloud.auth.reactive.rbac.HasLoginAdvice;
import com.jiangtj.cloud.auth.reactive.rbac.HasPermissionAdvice;
import com.jiangtj.cloud.auth.reactive.rbac.HasRoleAdvice;
import com.jiangtj.cloud.auth.reactive.rbac.HasTokenTypeAdvice;
import com.jiangtj.cloud.common.aop.AnnotationPointcut;
import jakarta.annotation.Resource;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class ReactiveAutoConfiguration {

    @Resource
    private AuthProperties authProperties;

    @Bean
    @LoadBalanced
    @ConditionalOnProperty(prefix="auth", name = "init-load-balanced-client", havingValue = "true", matchIfMissing = true)
    WebClient.Builder loadBalanced() {
        return WebClient.builder();
    }

    @Bean
    @ConditionalOnMissingBean
    AuthKeyLocator authKeyLocator() {
        return new ReactiveAuthKeyLocator();
    }

    @Bean
    public ReactiveTokenFilter reactiveTokenFilter() {
        return new ReactiveTokenFilter();
    }

    @Bean
    @ConditionalOnMissingBean(CoreTokenFilter.class)
    public ReactivePublicKeyFilter reactivePublicKeyFilter() {
        return new ReactivePublicKeyFilter();
    }

    @Bean
    public ReactiveNotifyService reactiveNotifyService() {
        return new ReactiveNotifyService();
    }

    @Bean
    public ReactiveCachedPublicKeyService reactiveCachedPublicKeyService() {
        return new ReactiveCachedPublicKeyService();
    }

    @Bean
    public ReactiveJWTExceptionHandler reactiveJWTExceptionHandler() {
        return new ReactiveJWTExceptionHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthWebClientFilter authWebClientFilter() {
        return new AuthWebClientFilter();
    }

    @Bean
    public HasLoginAdvice hasLoginAdvice() {
        return new HasLoginAdvice();
    }

    @Bean
    public HasTokenTypeAdvice hasTokenTypeAdvice() {
        return new HasTokenTypeAdvice();
    }

    @Bean
    public HasRoleAdvice hasRoleAdvice() {
        return new HasRoleAdvice();
    }

    @Bean
    public HasPermissionAdvice hasPermissionAdvice() {
        return new HasPermissionAdvice();
    }

    @Bean
    public Advisor hasLoginAdvisor(HasLoginAdvice advice) {
        return new DefaultPointcutAdvisor(new AnnotationPointcut<>(HasLogin.class), advice);
    }

    @Bean
    public Advisor hasTokenTypeAdvisor(HasTokenTypeAdvice advice) {
        return new DefaultPointcutAdvisor(new AnnotationPointcut<>(HasTokenType.class), advice);
    }

    @Bean
    public Advisor hasRoleAdvisor(HasRoleAdvice advice) {
        return new DefaultPointcutAdvisor(new AnnotationPointcut<>(HasRole.class), advice);
    }

    @Bean
    public Advisor hasPermissionAdvisor(HasPermissionAdvice advice) {
        return new DefaultPointcutAdvisor(new AnnotationPointcut<>(HasPermission.class), advice);
    }

}
