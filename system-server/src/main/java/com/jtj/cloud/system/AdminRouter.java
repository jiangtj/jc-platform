package com.jtj.cloud.system;

import com.jtj.cloud.common.BaseExceptionUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class AdminRouter {

	@Bean
	public RouterFunction<ServerResponse> adminRoutes() {
		return route()
			.GET("/insecure/fn/err", serverRequest -> {
				throw BaseExceptionUtils.badRequest("fn");
			})
			.GET("/insecure/fn/err2", serverRequest -> {
				throw new RuntimeException("系统错误");
			})
			.GET("/fn/needtoken", serverRequest -> {
				return ServerResponse.ok().bodyValue("ok");
			})
			.build();
	}
}
