package com.jtj.cloud.system;

import com.jtj.cloud.auth.reactive.AuthReactorUtils;
import com.jtj.cloud.common.reactive.BeanUtils;
import com.jtj.cloud.sql.reactive.PageUtils;
import com.jtj.cloud.system.dto.LoginResultDto;
import com.jtj.cloud.system.dto.PasswordUpdateDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.jtj.cloud.sql.reactive.IdUtils.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class SystemRouter {

    @Bean
    public RouterFunction<ServerResponse> indexRoutes() {
        return route()
            .GET("/", request -> ServerResponse.ok().bodyValue("System Client Started !!"))
            .build();
    }

    @Bean
    public RouterFunction<ServerResponse> loginRoutes(SystemUserService systemUserService) {
        return route()
            .POST("/login", request -> {
                Mono<LoginResultDto> result = request.bodyToMono(SystemUser.class)
                    .flatMap(systemUserService::login);
                return ServerResponse.ok().body(result, LoginResultDto.class);
            })
            .build();
    }

    @Bean
    public RouterFunction<ServerResponse> adminRoutes(SystemUserService systemUserService) {
        return route()
            .path("/admin", builder -> builder
                // 修改密码
                .POST("/user/password", request -> AuthReactorUtils.hasLogin()
                    .then(request.bodyToMono(PasswordUpdateDto.class))
                    .flatMap(dto -> {
                        Long userId = systemUserService.getRequiredCurrentUserId(request);
                        dto.setAdminId(userId);
                        return systemUserService.updateAdminPassword(dto);
                    })
                    .then(ServerResponse.ok().build()))

                // 增删改查
                .GET("/user/page", request -> {
                    PageRequest pageable = PageUtils.from(request);
                    Mono<Page<SystemUser>> many = AuthReactorUtils.hasPermission("system:user:read")
                        .then(BeanUtils.convertParams(request, new SystemUser()))
                        .flatMap(systemUser -> systemUserService.getAdminUserPage(systemUser, pageable));
                    return ServerResponse.ok().body(many, SystemUser.class);
                })

                .POST("/user", request -> AuthReactorUtils.hasPermission("system:user:write")
                    .then(request.bodyToMono(SystemUser.class))
                    .flatMap(systemUserService::createAdminUser)
                    .flatMap(result -> ServerResponse.ok().bodyValue(result)))

                .GET(idPath("/user"), request -> AuthReactorUtils.hasPermission("system:user:read")
                    .then(systemUserService.getAdminUser(idFrom(request)))
                    .flatMap(systemUser -> ServerResponse.ok().bodyValue(systemUser)))

                .PUT(idPath("/user"), request -> AuthReactorUtils.hasPermission("system:user:write")
                    .then(request.bodyToMono(SystemUser.class))
                    .doOnNext(item -> idFromNullable(request).ifPresent(item::setId))
                    .flatMap(systemUserService::updateAdminUser)
                    .flatMap(result -> ServerResponse.ok().bodyValue(result)))

                .DELETE(idPath("/user"), request -> AuthReactorUtils.hasPermission("system:user:write")
                    .then(systemUserService.deleteAdminUser(idFrom(request)))
                    .then(ServerResponse.noContent().build())))
            .build();
    }

    @Bean
    public RouterFunction<ServerResponse> roleRoutes(RoleService roleService) {
        return route()
            .GET("/role", request -> {
                return ServerResponse.ok().body(roleService.getRole(), new ParameterizedTypeReference<>() {});
            })
            .build();
    }

}
