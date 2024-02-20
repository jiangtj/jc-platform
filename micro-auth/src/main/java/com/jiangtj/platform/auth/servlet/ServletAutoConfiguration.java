
package com.jiangtj.platform.auth.servlet;


import com.jiangtj.platform.auth.annotations.HasLogin;
import com.jiangtj.platform.auth.annotations.HasPermission;
import com.jiangtj.platform.auth.annotations.HasRole;
import com.jiangtj.platform.auth.annotations.TokenType;
import com.jiangtj.platform.auth.servlet.rbac.HasLoginAdvice;
import com.jiangtj.platform.auth.servlet.rbac.HasPermissionAdvice;
import com.jiangtj.platform.auth.servlet.rbac.HasRoleAdvice;
import com.jiangtj.platform.auth.servlet.rbac.HasTokenTypeAdvice;
import com.jiangtj.platform.web.aop.AnnotationPointcut;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ServletAutoConfiguration {

    @Bean
    public ServletAuthContextFilter servletAuthContextFilter() {
        return new ServletAuthContextFilter();
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
        return new DefaultPointcutAdvisor(new AnnotationPointcut<>(TokenType.class), advice);
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
