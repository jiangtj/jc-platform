package com.jiangtj.platform.spring.cloud.client;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.reactive.DeferringLoadBalancerExchangeFilterFunction;
import org.springframework.context.ApplicationContext;
import org.springframework.web.reactive.function.client.WebClient;

public class TokenMutateWebClientBuilderBeanPostProcessor implements BeanPostProcessor {

	private final DeferringLoadBalancerExchangeFilterFunction loadBalancedFilter;
	private final TokenMutateExchangeFilterFunction tokenMutateFilter;

	private final ApplicationContext context;

	public TokenMutateWebClientBuilderBeanPostProcessor(
		DeferringLoadBalancerExchangeFilterFunction loadBalancedFilter,
		TokenMutateExchangeFilterFunction tokenMutateFilter,
		ApplicationContext context) {
		this.loadBalancedFilter = loadBalancedFilter;
		this.tokenMutateFilter = tokenMutateFilter;
		this.context = context;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof WebClient.Builder) {
			if (context.findAnnotationOnBean(beanName, ClientMutator.class) == null) {
				return bean;
			}
			if (context.findAnnotationOnBean(beanName, LoadBalanced.class) == null) {
				((WebClient.Builder) bean).filter(loadBalancedFilter);
			}
			((WebClient.Builder) bean).filters(filters -> filters.add(0, tokenMutateFilter));
		}
		return bean;
	}

}
