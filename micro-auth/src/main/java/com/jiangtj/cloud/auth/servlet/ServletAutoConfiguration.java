
package com.jiangtj.cloud.auth.servlet;


import com.jiangtj.cloud.auth.rbac.annotations.HasLogin;
import com.jiangtj.cloud.auth.servlet.rbac.HasLoginAdvice;
import com.jiangtj.cloud.common.aop.AnnotationPointcut;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ServletAutoConfiguration {

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

}
