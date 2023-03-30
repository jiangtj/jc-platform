package com.jtj.cloud.system;

import com.jtj.cloud.common.reactive.BeanUtils;
import com.jtj.cloud.common.reactive.PageUtils;
import com.jtj.cloud.system.dto.LoginResultDto;
import com.jtj.cloud.system.dto.PasswordUpdateDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.jtj.cloud.common.reactive.IdUtils.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class AdminRouter {

    @Bean
    public RouterFunction<ServerResponse> loginRoutes(AdminService adminService) {
        return route()
            .POST("/login", request -> {
                Mono<LoginResultDto> result = request.bodyToMono(AdminUser.class)
                    .flatMap(adminService::login);
                return ServerResponse.ok().body(result, LoginResultDto.class);
            })
            .POST("/token/refresh", request -> {
                Long userId = adminService.getRequiredCurrentUserId(request);
                Mono<String> result = adminService.refreshToken(userId);
                return ServerResponse.ok().body(result, String.class);
            })
            .build();
    }

    @Bean
    public RouterFunction<ServerResponse> adminRoutes(AdminService adminService) {
        return route()
            .path("/admin", builder -> builder
                // 修改密码
                .POST("/user/password", request -> request
                    .bodyToMono(PasswordUpdateDto.class)
                    .flatMap(dto -> {
                        Long userId = adminService.getRequiredCurrentUserId(request);
                        dto.setAdminId(userId);
                        return adminService.updateAdminPassword(dto);
                    })
                    .then(ServerResponse.ok().build()))

                // 增删改查
                .GET("/user/page", request -> {
                    PageRequest pageable = PageUtils.from(request);
                    Mono<Page<AdminUser>> many = BeanUtils.convertParams(request, new AdminUser())
                        .flatMap(adminUser -> adminService.getAdminUserPage(adminUser, pageable));
                    // return BeanUtils.convertParams(request, new AdminUser())
                    //     .flatMapMany(adminUser -> adminService.getAdminUserPage(adminUser, pageable))
                    //     .collect(Collectors.toList())
                    //     .flatMap(result -> ServerResponse.ok().bodyValue(result));
                    return ServerResponse.ok().body(many, AdminUser.class);
                })

                .POST("/user", request -> request.bodyToMono(AdminUser.class)
                    .flatMap(adminService::createAdminUser)
                    .flatMap(result -> ServerResponse.ok().bodyValue(result)))

                .GET(idPath("/user"), request -> adminService.getAdminUser(idFrom(request))
                    .flatMap(adminUser -> ServerResponse.ok().bodyValue(adminUser)))

                .PUT(idPath("/user"), request -> request.bodyToMono(AdminUser.class)
                    .doOnNext(item -> idFromNullable(request).ifPresent(item::setId))
                    .flatMap(adminService::updateAdminUser)
                    .flatMap(result -> ServerResponse.ok().bodyValue(result)))

                .DELETE(idPath("/user"), request -> adminService.deleteAdminUser(idFrom(request))
                    .then(ServerResponse.noContent().build()))
            )
            .build();
    }
}
