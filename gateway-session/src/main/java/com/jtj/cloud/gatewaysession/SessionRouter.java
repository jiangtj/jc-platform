package com.jtj.cloud.gatewaysession;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class SessionRouter {

    @Bean
    public RouterFunction<ServerResponse> sessionRoutes() {
        return route()
            .GET("/set/session/{id}", request -> {
                String id = request.pathVariable("id");
                return request.exchange().getSession().flatMap(webSession -> {
                    webSession.getAttributes().put("admin", id);
                    return ServerResponse.ok().bodyValue("session 设置成功");
                });
            })
            .build();
    }
}
