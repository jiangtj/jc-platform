package com.jtj.cloud.baseservlet;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import com.jtj.cloud.common.BaseExceptionUtils;


@Configuration
public class BaseRouter {

	@Bean
	public RouterFunction<ServerResponse> baseRoutes() {
		return RouterFunctions.route()
				.GET("/insecure/fn/err", serverRequest -> {
					throw BaseExceptionUtils.invalidToken("fn");
				})
				.GET("/fn/needtoken", serverRequest -> {
					return ServerResponse.ok().body("ok");
				})
				.build();
	}
}
