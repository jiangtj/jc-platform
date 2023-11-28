package com.jiangtj.platform.baseservlet;

import com.jiangtj.platform.common.BaseExceptionUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;


@Configuration
public class BaseRouter {

    @Bean
    public RouterFunction<ServerResponse> baseRoutes() {
        return RouterFunctions.route()
            .GET("/insecure/fn/err", serverRequest -> {
                throw BaseExceptionUtils.badRequest("fn");
            })
            .GET("/insecure/fn/err2", serverRequest -> {
                throw new RuntimeException("系统错误");
            })
            .GET("/fn/needtoken", serverRequest -> {
                return ServerResponse.ok().body("ok");
            })
            .build();
    }
}
