package com.jiangtj.platform.spring.cloud.client;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.ApplicationContext;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;

public class TokenMutateRestClientBuilderBeanPostProcessor implements BeanPostProcessor {

	private final ClientHttpRequestInterceptor loadBalancedInterceptor;
	private final TokenMutateHttpRequestInterceptor tokenMutateInterceptor;

	private final ApplicationContext context;

	public TokenMutateRestClientBuilderBeanPostProcessor(
		ClientHttpRequestInterceptor loadBalancerInterceptor,
		TokenMutateHttpRequestInterceptor tokenMutateInterceptor,
		ApplicationContext context) {
		this.loadBalancedInterceptor = loadBalancerInterceptor;
		this.tokenMutateInterceptor = tokenMutateInterceptor;
		this.context = context;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof RestClient.Builder) {
			if (context.findAnnotationOnBean(beanName, ClientMutator.class) == null) {
				return bean;
			}
			if (context.findAnnotationOnBean(beanName, LoadBalanced.class) == null) {
				((RestClient.Builder) bean).requestInterceptor(loadBalancedInterceptor);
			}
			((RestClient.Builder) bean).requestInterceptors(c -> c.add(0, tokenMutateInterceptor));
		}
		return bean;
	}

}
