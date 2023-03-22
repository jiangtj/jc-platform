package com.jtj.cloud.baseservlet;

import com.jtj.cloud.common.BaseExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;


@Slf4j
@Configuration
public class BaseRouter {

    @Bean
    public RouterFunction<ServerResponse> baseRoutes() {
        return RouterFunctions.route()
            .GET("/insecure/fn/err", serverRequest -> {
                throw BaseExceptionUtils.badRequest("fn");
            })
            .GET("/fn/needtoken", serverRequest -> {
                return ServerResponse.ok().body("ok");
            })
            .build();
    }
}
