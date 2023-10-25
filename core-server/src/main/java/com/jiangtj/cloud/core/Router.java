package com.jiangtj.cloud.core;

import com.jiangtj.cloud.core.pk.PublicKeyService;
import com.jiangtj.cloud.core.pk.UpdateDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class Router {

	@Bean
	public RouterFunction<ServerResponse> pkRoutes(PublicKeyService publicKeyService) {
		return route()
			.GET("/services/publickey", serverRequest -> {
				return ServerResponse.ok().body(publicKeyService.getPublicJwks(),
					new ParameterizedTypeReference<>() {});
			})
			.GET("/service/{id}/publickey", serverRequest -> {
				return ServerResponse.ok().body(publicKeyService.getPublicKey(serverRequest.pathVariable("id")),
					new ParameterizedTypeReference<>() {});
			})
			.PUT("/service/publickey", request -> {
				return request.bodyToMono(UpdateDto.class)
					.flatMap(publicKeyService::updatePublicKey)
					.then(ServerResponse.noContent().build());
			})
			.build();
	}
}
