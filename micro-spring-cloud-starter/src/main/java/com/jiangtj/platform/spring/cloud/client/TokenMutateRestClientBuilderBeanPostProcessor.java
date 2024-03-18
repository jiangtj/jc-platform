package com.jiangtj.platform.spring.cloud.client;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.ApplicationContext;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;

public class TokenMutateRestClientBuilderBeanPostProcessor implements BeanPostProcessor {

	private final ClientHttpRequestInterceptor loadBalancerInterceptor;

	private final ApplicationContext context;

	public TokenMutateRestClientBuilderBeanPostProcessor(ClientHttpRequestInterceptor loadBalancerInterceptor,
                                                         ApplicationContext context) {
		this.loadBalancerInterceptor = loadBalancerInterceptor;
		this.context = context;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof RestClient.Builder) {
			if (context.findAnnotationOnBean(beanName, LoadBalanced.class) == null) {
				return bean;
			}
			((RestClient.Builder) bean).requestInterceptor(loadBalancerInterceptor);
		}
		return bean;
	}

}
