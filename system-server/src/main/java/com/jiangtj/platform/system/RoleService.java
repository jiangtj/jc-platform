package com.jiangtj.platform.system;

import com.jiangtj.platform.auth.AuthRequestAttributes;
import com.jiangtj.platform.auth.KeyUtils;
import com.jiangtj.platform.spring.cloud.AuthServer;
import com.jiangtj.platform.spring.cloud.system.Role;
import com.jiangtj.platform.spring.cloud.system.RoleEndpoint;
import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@Slf4j
@Service
public class RoleService {

    @Resource
    DiscoveryClient discoveryClient;
    @Value("${spring.application.name}")
    String selfName;
    @Resource
    RoleEndpoint roleEndpoint;
    @Resource
    RestClient.Builder loadBalancedClient;
    @Resource
    AuthServer authServer;

    @Getter
    List<ServerRole> serverRoles;

    @Scheduled(initialDelay = 15, fixedDelay = 60, timeUnit = TimeUnit.SECONDS)
    public void fetchAndCacheRole() {
        List<String> services = discoveryClient.getServices();
        serverRoles = services.stream()
            .map(s -> {
                if (selfName.equals(s)) {
                    return new ServerRole(s, roleEndpoint.roles());
                }
                String token = authServer.createServerToken(s);
                List<String> roles = loadBalancedClient.build().get()
                    .uri("http://" + s + "/actuator/role")
                    .header(AuthRequestAttributes.TOKEN_HEADER_NAME, token)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<String>>() {
                    });
                return new ServerRole(s, roles);
            })
            .toList();
        /*Flux.fromIterable(discoveryClient.getServices())
            .flatMap(s -> {
                if (selfName.equals(s)) {
                    return Mono.just(roleEndpoint.roles())
                        .map(r -> new ServerRole(s, r));
                }

                String token = authServer.createServerToken(s);

                return webClient.build().get()
                    .uri("http://" + s + "/actuator/role")
                    .headers(httpHeaders -> {
                        httpHeaders.remove(AuthRequestAttributes.TOKEN_HEADER_NAME);
                        httpHeaders.add(AuthRequestAttributes.TOKEN_HEADER_NAME, token);
                    })
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<String>>() {
                    })
                    .onErrorResume(WebClientResponseException.class, e -> {
                        log.error("call actuator role error", e);
                        return Mono.just(Collections.emptyList());
                    })
                    .map(r -> new ServerRole(s, r));
            })
            .log()
            .collectList()
            .subscribe(roles -> {
                serverRoles = roles;
                log.error(JsonUtils.toJson(serverRoles));
            });*/
    }

    public Stream<ServerRole> getServerRoleKeysStream() {
        return serverRoles.stream()
            .map(sr -> {
                List<String> list = sr.roles().stream().map(KeyUtils::toKey).toList();
                return new ServerRole(sr.server(), list);
            });
    }

    public List<ServerRole> getServerRoleKeys() {
        return getServerRoleKeysStream().toList();
    }

    public List<Role> getRoleInfo(String key) {
        return getServerRoleKeysStream()
            .filter(sr -> sr.roles().contains(key))
            .map(sr -> loadBalancedClient.build().get()
                .uri("http://" + sr.server() + "/actuator/role/" + key)
                .retrieve()
                .body(Role.class))
            .toList();
                /*.onErrorResume(WebClientResponseException.class, e -> {
                    ProblemDetail detail = e.getResponseBodyAs(ProblemDetail.class);
                    if (detail == null) {
                        detail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                    return Mono.error(new BaseException(detail));
                }))*/
    }

}
