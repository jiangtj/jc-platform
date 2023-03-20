package com.jtj.cloud.basereactive;

import com.jtj.cloud.common.BaseExceptionUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class BaseRouter {

	@Bean
	public RouterFunction<ServerResponse> baseRoutes() {
		return route()
			.GET("/insecure/fn/err", serverRequest -> {
				throw BaseExceptionUtils.invalidToken("fn");
			})
			.GET("/fn/needtoken", serverRequest -> {
				return ServerResponse.ok().bodyValue("ok");
			})
			.build();
	}
}
