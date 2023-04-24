package com.jtj.cloud.system;

import com.jtj.cloud.auth.reactive.AuthReactorUtils;
import com.jtj.cloud.common.reactive.BeanUtils;
import com.jtj.cloud.sql.reactive.PageUtils;
import com.jtj.cloud.system.dto.LoginDto;
import com.jtj.cloud.system.dto.LoginResultDto;
import com.jtj.cloud.system.dto.PasswordUpdateDto;
import com.jtj.cloud.system.entity.SystemUser;
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
public class RouterConfiguration {

    @Bean
    public RouterFunction<ServerResponse> indexRoutes() {
        return route()
            .GET("/", request -> ServerResponse.ok().bodyValue("System Client Started !!"))
            .build();
    }

    @Bean
    public RouterFunction<ServerResponse> loginRoutes(UserService userService) {
        return route()
            .POST("/login", request -> {
                Mono<LoginResultDto> result = request.bodyToMono(LoginDto.class)
                    .flatMap(userService::login);
                return ServerResponse.ok().body(result, LoginResultDto.class);
            })
            .build();
    }

    @Bean
    public RouterFunction<ServerResponse> adminRoutes(UserService userService) {
        return route()
            .POST("/user/password", request -> AuthReactorUtils.hasLogin()
                .then(request.bodyToMono(PasswordUpdateDto.class))
                .flatMap(dto -> userService.getRequiredCurrentUserId()
                    .flatMap(userId -> {
                        dto.setAdminId(userId);
                        return userService.updateAdminPassword(dto);
                    }))
                .then(ServerResponse.ok().build()))

            .GET("/user/page", request -> {
                PageRequest pageable = PageUtils.from(request);
                Mono<Page<SystemUser>> many = AuthReactorUtils.hasPermission("system:user:read")
                    .then(BeanUtils.convertParams(request, new SystemUser()))
                    .flatMap(systemUser -> userService.getAdminUserPage(systemUser, pageable));
                return ServerResponse.ok().body(many, new ParameterizedTypeReference<>() {});
            })

            .POST("/user", request -> AuthReactorUtils.hasPermission("system:user:write")
                .then(request.bodyToMono(SystemUser.class))
                .flatMap(userService::createAdminUser)
                .flatMap(result -> ServerResponse.ok().bodyValue(result)))

            .GET(idPath("/user"), request -> AuthReactorUtils.hasPermission("system:user:read")
                .then(userService.getAdminUser(idFrom(request)))
                .flatMap(systemUser -> ServerResponse.ok().bodyValue(systemUser)))

            .PUT(idPath("/user"), request -> AuthReactorUtils.hasPermission("system:user:write")
                .then(request.bodyToMono(SystemUser.class))
                .doOnNext(item -> idFromNullable(request).ifPresent(item::setId))
                .flatMap(userService::updateAdminUser)
                .flatMap(result -> ServerResponse.ok().bodyValue(result)))

            .DELETE(idPath("/user"), request -> AuthReactorUtils.hasPermission("system:user:write")
                .then(userService.deleteAdminUser(idFrom(request)))
                .then(ServerResponse.noContent().build()))
            .build();
    }

    @Bean
    public RouterFunction<ServerResponse> roleRoutes(RoleService roleService) {
        return route()
            .GET("/roles/name", request -> ServerResponse.ok()
                .body(roleService.getServerRoles(), new ParameterizedTypeReference<>() {}))
            .GET("/roles/key", request -> ServerResponse.ok()
                .body(roleService.getServerRoleKeys(), new ParameterizedTypeReference<>() {}))
            .GET("/role/{key}", request -> ServerResponse.ok()
                .body(roleService.getRoleInfo(request.pathVariable("key")), new ParameterizedTypeReference<>() {}))
            .build();
    }

}
