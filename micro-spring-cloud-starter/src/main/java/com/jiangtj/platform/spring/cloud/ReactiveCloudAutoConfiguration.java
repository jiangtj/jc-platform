package com.jiangtj.platform.spring.cloud;


import com.jiangtj.platform.spring.cloud.client.TokenMutateExchangeFilterFunction;
import com.jiangtj.platform.spring.cloud.client.TokenMutateWebClientBuilderBeanPostProcessor;
import com.jiangtj.platform.spring.cloud.core.CoreTokenExchangeFilterFunction;
import com.jiangtj.platform.spring.cloud.core.ReactiveCoreInstanceApi;
import com.jiangtj.platform.spring.cloud.jwt.AuthKeyLocator;
import com.jiangtj.platform.spring.cloud.jwt.ReactiveJWTExceptionHandler;
import com.jiangtj.platform.spring.cloud.server.ServerProviderReactorWebFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cloud.client.loadbalancer.reactive.DeferringLoadBalancerExchangeFilterFunction;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@AutoConfiguration(after = AuthCloudAutoConfiguration.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class ReactiveCloudAutoConfiguration {
    @Bean
    public ReactiveJWTExceptionHandler reactiveJWTExceptionHandler() {
        return new ReactiveJWTExceptionHandler();
    }

    @Bean
    public TokenMutateExchangeFilterFunction tokenMutateExchangeFilterFunction() {
        return new TokenMutateExchangeFilterFunction();
    }

    @Bean
    public TokenMutateWebClientBuilderBeanPostProcessor tokenMutateWebClientBuilderBeanPostProcessor(
        DeferringLoadBalancerExchangeFilterFunction loadBalancedFilter,
        TokenMutateExchangeFilterFunction tokenMutateFilter,
        ApplicationContext context
    ) {
        return new TokenMutateWebClientBuilderBeanPostProcessor(loadBalancedFilter, tokenMutateFilter, context);
    }

    @Bean
    public CoreTokenExchangeFilterFunction coreTokenExchangeFilterFunction() {
        return new CoreTokenExchangeFilterFunction();
    }

    @Bean
    @ConditionalOnMissingBean
    public ReactiveCoreInstanceApi coreInstanceApi(DeferringLoadBalancerExchangeFilterFunction deferringExchangeFilterFunction,
                                           CoreTokenExchangeFilterFunction coreTokenExchangeFilterFunction) {
        WebClient webClient = WebClient.builder()
            .baseUrl("lb://core-server/")
            .filter(deferringExchangeFilterFunction)
            .filter(coreTokenExchangeFilterFunction)
            .build();
        WebClientAdapter adapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(ReactiveCoreInstanceApi.class);
    }

    @Bean
    @ConditionalOnMissingBean
    AuthKeyLocator authKeyLocator() {
        return new ReactiveAuthKeyLocator();
    }

    @Bean
    public ReactivePublicKeyFilter reactivePublicKeyFilter() {
        return new ReactivePublicKeyFilter();
    }

    @Bean
    @ConditionalOnProperty(prefix = "jcloud.auth.provider.server", name = "filter-enable", matchIfMissing = true)
    public ServerProviderReactorWebFilter serverProviderReactorWebFilter() {
        return new ServerProviderReactorWebFilter();
    }

}
