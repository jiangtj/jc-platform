package com.jtj.cloud.system;

import com.jtj.cloud.auth.AuthServer;
import com.jtj.cloud.auth.TokenType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class TokenRouter {

    @Bean
    public RouterFunction<ServerResponse> tokenRoutes(AuthServer authServer) {
        return route()
            .GET("/insecure/token", serverRequest -> {
                String token = authServer.builder()
                    .setAuthType(TokenType.SYSTEM)
                    .build();
                return ServerResponse.ok().bodyValue(token);
            })
            .build();
    }
}
