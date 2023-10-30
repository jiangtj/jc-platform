
package com.jiangtj.cloud.auth.servlet;


import com.jiangtj.cloud.auth.AuthKeyLocator;
import com.jiangtj.cloud.auth.rbac.annotations.HasLogin;
import com.jiangtj.cloud.auth.servlet.rbac.HasLoginAdvice;
import com.jiangtj.cloud.common.aop.AnnotationPointcut;
import feign.RequestInterceptor;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ServletAutoConfiguration {

    @Bean
    @LoadBalanced
    RestTemplate loadBalanced() {
        return new RestTemplate();
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthKeyLocator authKeyLocator() {
        return new ServletAuthKeyLocator();
    }

    @Bean
    public ServletNotifyService servletNotifyService() {
        return new ServletNotifyService();
    }

    @Bean
    public ServletTokenFilter servletTokenFilter() {
        return new ServletTokenFilter();
    }

    @Bean
    public ServletJWTExceptionHandler servletJWTExceptionHandler() {
        return new ServletJWTExceptionHandler();
    }

    @Bean
    public HasLoginAdvice hasLoginAdvice() {
        return new HasLoginAdvice();
    }

    @Bean
    public Advisor hasLoginAdvisor(HasLoginAdvice advice) {
        return new DefaultPointcutAdvisor(new AnnotationPointcut<>(HasLogin.class), advice);
    }

    @Bean
    @ConditionalOnClass(RequestInterceptor.class)
    public AuthFeignInterceptor authFeignInterceptor() {
        return new AuthFeignInterceptor();
    }

}
