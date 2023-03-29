package com.jtj.cloud.basereactive;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ProblemDetail;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.jtj.cloud.auth.reactive.AuthWebClientFiler;

import java.util.Objects;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class BaseServletRouter {

    @Bean
    public RouterFunction<ServerResponse> baseServletRoutes(WebClient.Builder webClient, AuthWebClientFiler authFiler) {
        return route()
            .GET("/insecure/servlet/home", serverRequest ->
                webClient.build().get().uri("http://base-servlet/")
                    .retrieve()
                    .bodyToMono(String.class)
                    .flatMap(result -> ServerResponse.ok().bodyValue(result)))
            .GET("/insecure/servlet/needtoken", serverRequest ->
                webClient.filter(authFiler.filter(serverRequest.exchange())).build()
                    .get().uri("http://base-servlet/needtoken")
                    .retrieve()
                    .bodyToMono(String.class)
                    .flatMap(result -> ServerResponse.ok().bodyValue(result))
                    .onErrorResume(WebClientResponseException.class, e -> {
                        ProblemDetail detail = e.getResponseBodyAs(ProblemDetail.class);
                        Objects.requireNonNull(detail);
                        return ServerResponse.status(detail.getStatus()).bodyValue(detail);
                    }))
            .build();
    }
}
