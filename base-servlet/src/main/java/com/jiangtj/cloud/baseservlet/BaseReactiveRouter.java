package com.jiangtj.cloud.baseservlet;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.web.servlet.function.RouterFunctions.route;


@Configuration
public class BaseReactiveRouter {

    @Bean
    public RouterFunction<ServerResponse> baseServletRoutes(BaseReactiveClient client) {
        return route()
            .GET("/insecure/reactive/home", serverRequest ->
                ServerResponse.ok().body(client.getIndex()))
            .GET("/insecure/reactive/needtoken", serverRequest ->
                ServerResponse.ok().body(client.getNeedToken()))
            .build();
    }
}
