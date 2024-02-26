package com.jiangtj.platform.core;

import com.jiangtj.platform.core.pk.PublicKeyService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.web.servlet.function.RouterFunctions.route;

@Configuration
public class Router {

	@Bean
	public RouterFunction<ServerResponse> pkRoutes(PublicKeyService publicKeyService) {
		return route()
			.GET("/services", serverRequest ->
				ServerResponse.ok().body(publicKeyService.getAllCoreServiceInstance()))
			.GET("/service/{id}/publickey", serverRequest ->
				ServerResponse.ok().body(
					publicKeyService.getPublicKey(serverRequest.pathVariable("id"))))
			.build();
	}
}
