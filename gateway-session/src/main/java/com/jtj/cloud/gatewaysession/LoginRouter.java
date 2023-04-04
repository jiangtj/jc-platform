package com.jtj.cloud.gatewaysession;

import com.jtj.cloud.common.BaseExceptionUtils;
import com.jtj.cloud.common.JsonUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class LoginRouter {

    @Bean
    public RouterFunction<ServerResponse> loginRoutes(WebClient.Builder webClient) {
        return route()
            // 获取登录信息
            .GET("/login/info", request ->
                request.session().<AdminUser>handle((webSession, sink) -> {
                        String temp = webSession.getAttribute("admin");
                        if (temp == null) {
                            sink.error(BaseExceptionUtils.unauthorized("未登录!"));
                            return;
                        }
                        sink.next(JsonUtil.fromJson(temp, AdminUser.class));
                    })
                    .flatMap(result -> ServerResponse.ok().bodyValue(result)))
            // 登录
            .POST("/login", request ->
                webClient.build().post().uri("http://system-server/login")
                    .body(request.bodyToMono(LoginDto.class), LoginDto.class)
                    .retrieve()
                    .bodyToMono(AdminUser.class)
                    .flatMap(adminUser ->
                        request.session().map(webSession -> {
                            webSession.getAttributes().put("admin-id", adminUser.getId());
                            webSession.getAttributes().put("admin", JsonUtil.toJson(adminUser));
                            return adminUser;}))
                    .flatMap(result -> ServerResponse.ok().bodyValue(result)))
            .build();
    }
}
