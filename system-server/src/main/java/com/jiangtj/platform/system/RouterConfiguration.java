package com.jiangtj.platform.system;

import com.jiangtj.platform.auth.servlet.AuthUtils;
import com.jiangtj.platform.spring.cloud.system.RoleService;
import com.jiangtj.platform.spring.cloud.system.RoleSyncDto;
import com.jiangtj.platform.system.dto.LoginDto;
import com.jiangtj.platform.system.dto.LoginResultDto;
import com.jiangtj.platform.system.dto.PasswordUpdateDto;
import com.jiangtj.platform.system.entity.SystemUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.List;

import static com.jiangtj.platform.system.IdUtils.*;
import static org.springframework.web.servlet.function.RouterFunctions.route;

@Configuration
public class RouterConfiguration {

    @Bean
    public RouterFunction<ServerResponse> indexRoutes() {
        return route()
            .GET("/", request -> ServerResponse.ok().body("System Client Started !!"))
            .build();
    }

    @Bean
    public RouterFunction<ServerResponse> loginRoutes(UserService userService) {
        return route()
            .POST("/login", request -> {
                LoginDto dto = request.body(LoginDto.class);
                LoginResultDto result = userService.login(dto);
                return ServerResponse.ok().body(result);
            })
            .build();
    }

    @Bean
    public RouterFunction<ServerResponse> adminRoutes(UserService userService) {
        return route()
            .POST("/user/password", request -> {
                AuthUtils.hasLogin();
                PasswordUpdateDto dto = request.body(PasswordUpdateDto.class);
                userService.updateAdminPassword(dto);
                return ServerResponse.ok().build();
            })

            .GET("/user/page", request -> {
                AuthUtils.hasPermission("system:user:read");
                PageRequest pageable = PageRequestUtils.from(request);
                SystemUser user = request.bind(SystemUser.class);
                Page<SystemUser> page = userService.getAdminUserPage(user, pageable);
                return ServerResponse.ok().body(page, new ParameterizedTypeReference<>() {});
            })

            .POST("/user", request -> {
                AuthUtils.hasPermission("system:user:write");
                SystemUser user = request.body(SystemUser.class);
                SystemUser result = userService.createAdminUser(user);
                return ServerResponse.ok().body(result);
            })

            .GET(idPath("/user"), request -> {
                AuthUtils.hasPermission("system:user:read");
                SystemUser user = userService.getAdminUser(idFrom(request));
                return ServerResponse.ok().body(user);
            })

            .PUT(idPath("/user"), request -> {
                AuthUtils.hasPermission("system:user:write");
                SystemUser user = request.body(SystemUser.class);
                idFromNullable(request).ifPresent(user::setId);
                SystemUser result = userService.updateAdminUser(user);
                return ServerResponse.ok().body(result);
            })

            .DELETE(idPath("/user"), request -> {
                AuthUtils.hasPermission("system:user:write");
                userService.deleteAdminUser(idFrom(request));
                return ServerResponse.noContent().build();
            })
            .build();
    }

    @Bean
    public RouterFunction<ServerResponse> userRoleRoutes(UserRoleService userRoleService) {
        return route()
            .GET("/xsafef", request -> ServerResponse.ok().body("System Client Started !!"))
            .GET("/user/{id}/roles", request -> ServerResponse.ok()
                .body(userRoleService.getUserRoles(Long.valueOf(request.pathVariable("id"))),
                    new ParameterizedTypeReference<>() {}))
            .PUT("/user/{id}/roles", request -> {
                List<String> body = request.body(new ParameterizedTypeReference<>() {});
                List<String> result = userRoleService.updateUserRoles(Long.valueOf(request.pathVariable("id")), body);
                return ServerResponse.ok().body(result);
            })
            .build();
    }

    @Bean
    public RouterFunction<ServerResponse> roleSyncRoutes(RoleService roleService) {
        return route()
            .POST("/roles/sync", request -> {
                List<RoleSyncDto> roles = request.body(new ParameterizedTypeReference<>() {});
                roleService.sync(roles);
                return ServerResponse.ok().build();
            })
            .build();
    }

    @Bean
    public RouterFunction<ServerResponse> roleRoutes(RoleServiceImpl roleService) {
        return route()
            .filter((request, next) -> {
                AuthUtils.hasPermission("system:role");
                return next.handle(request);
            })
            .GET("/roles/name", request -> ServerResponse.ok()
                .body(roleService.getServerRoles(), new ParameterizedTypeReference<>() {}))
            .GET("/roles/key", request -> ServerResponse.ok()
                .body(roleService.getServerRoleKeys(), new ParameterizedTypeReference<>() {}))
            .GET("/role/{key}", request -> ServerResponse.ok()
                .body(roleService.getRoleInfo(request.pathVariable("key")), new ParameterizedTypeReference<>() {}))
            .build();
    }

}
