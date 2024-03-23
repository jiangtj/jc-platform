
package com.jiangtj.platform.spring.cloud;


import com.jiangtj.platform.spring.cloud.client.TokenMutateHttpRequestInterceptor;
import com.jiangtj.platform.spring.cloud.client.TokenMutateRestClientBuilderBeanPostProcessor;
import com.jiangtj.platform.spring.cloud.core.CoreInstanceApi;
import com.jiangtj.platform.spring.cloud.core.CoreTokenInterceptor;
import com.jiangtj.platform.spring.cloud.jwt.AuthKeyLocator;
import com.jiangtj.platform.spring.cloud.jwt.ServletJWTExceptionHandler;
import com.jiangtj.platform.spring.cloud.server.ServerProviderOncePerRequestFilter;
import com.jiangtj.platform.spring.cloud.server.ServerToken;
import com.jiangtj.platform.spring.cloud.server.ServerTokenMvcAdvice;
import com.jiangtj.platform.web.aop.AnnotationPointcut;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@AutoConfiguration(after = AuthCloudAutoConfiguration.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ServletCloudAutoConfiguration {
    @Bean
    public ServletJWTExceptionHandler servletJWTExceptionHandler() {
        return new ServletJWTExceptionHandler();
    }

    @Bean
    public CoreTokenInterceptor coreTokenInterceptor() {
        return new CoreTokenInterceptor();
    }

    @Bean
    public TokenMutateHttpRequestInterceptor tokenMutateHttpRequestInterceptor() {
        return new TokenMutateHttpRequestInterceptor();
    }

    @Bean
    public TokenMutateRestClientBuilderBeanPostProcessor tokenMutateRestClientBuilderBeanPostProcessor(
        LoadBalancerInterceptor loadBalancerInterceptor,
        TokenMutateHttpRequestInterceptor tokenMutateHttpRequestInterceptor,
        ApplicationContext context
    ) {
        return new TokenMutateRestClientBuilderBeanPostProcessor(loadBalancerInterceptor, tokenMutateHttpRequestInterceptor, context);
    }

    @Bean
    @ConditionalOnMissingBean
    public CoreInstanceApi coreInstanceApi(LoadBalancerInterceptor loadBalancerInterceptor,
                                           CoreTokenInterceptor coreTokenInterceptor) {
        RestClient restClient = RestClient.builder()
            .baseUrl("lb://core-server/")
            .requestInterceptor(loadBalancerInterceptor)
            .requestInterceptor(coreTokenInterceptor)
            .build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(CoreInstanceApi.class);
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthKeyLocator authKeyLocator() {
        return new ServletAuthKeyLocator();
    }

    @Bean
    @ConditionalOnProperty(prefix = "jcloud.auth.provider.server", name = "filter-enable", matchIfMissing = true)
    public ServerProviderOncePerRequestFilter serverProviderOncePerRequestFilter() {
        return new ServerProviderOncePerRequestFilter();
    }

    @Bean
    public ServerTokenMvcAdvice serverTokenMvcAdvice() {
        return new ServerTokenMvcAdvice();
    }

    @Bean
    public Advisor serverTokenAdvisor(ServerTokenMvcAdvice advice) {
        return new DefaultPointcutAdvisor(new AnnotationPointcut<>(ServerToken.class), advice);
    }

}
