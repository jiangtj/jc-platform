package com.jtj.cloud.gatewaysession;

import com.jtj.cloud.common.BaseExceptionUtils;
import com.jtj.cloud.common.JsonUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ProblemDetail;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.Objects;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class LoginRouter {

    @Bean
    public RouterFunction<ServerResponse> loginRoutes(WebClient.Builder webClient) {
        return route()
            // 获取登录信息
            .GET("/login/info", request ->
                request.session().<SystemUser>handle((webSession, sink) -> {
                        String temp = webSession.getAttribute("admin");
                        if (temp == null) {
                            sink.error(BaseExceptionUtils.unauthorized("未登录!"));
                            return;
                        }
                        sink.next(JsonUtil.fromJson(temp, SystemUser.class));
                    })
                    .flatMap(result -> ServerResponse.ok().bodyValue(result)))
            // 登录
            .POST("/login", request ->
                webClient.build().post().uri("http://system-server/login")
                    .body(request.bodyToMono(LoginDto.class), LoginDto.class)
                    .retrieve()
                    .bodyToMono(LoginResultDto.class)
                    .flatMap(result ->
                        request.session().map(webSession -> {
                            webSession.getAttributes().put("admin-claims", result.getClaims());
                            webSession.getAttributes().put("admin", JsonUtil.toJson(result.getUser()));
                            return result.getUser();}))
                    .flatMap(result -> ServerResponse.ok().bodyValue(result))
                    .onErrorResume(WebClientResponseException.class, e -> {
                        ProblemDetail detail = e.getResponseBodyAs(ProblemDetail.class);
                        Objects.requireNonNull(detail);
                        return ServerResponse.status(detail.getStatus()).bodyValue(detail);
                    }))
            .build();
    }
}
