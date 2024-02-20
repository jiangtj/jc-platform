package com.jiangtj.platform.system;

import com.jiangtj.platform.auth.AuthRequestAttributes;
import com.jiangtj.platform.auth.KeyUtils;
import com.jiangtj.platform.auth.cloud.AuthServer;
import com.jiangtj.platform.auth.cloud.system.Role;
import com.jiangtj.platform.auth.cloud.system.RoleEndpoint;
import com.jiangtj.platform.common.JsonUtils;
import com.jiangtj.platform.web.BaseException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    WebClient.Builder webClient;
    @Resource
    AuthServer authServer;

    List<ServerRole> serverRoles;

    @Scheduled(initialDelay = 15, fixedDelay = 60, timeUnit = TimeUnit.SECONDS)
    public void fetchAndCacheRole() {
        Flux.fromIterable(discoveryClient.getServices())
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
            });
    }

    public Flux<ServerRole> getServerRoles() {
        return Flux.fromIterable(serverRoles);
    }

    public Flux<ServerRole> getServerRoleKeys() {
        return Flux.fromIterable(serverRoles)
            .map(sr -> {
                List<String> list = sr.roles().stream().map(KeyUtils::toKey).toList();
                return new ServerRole(sr.server(), list);
            });
    }

    public Flux<Role> getRoleInfo(String key) {
        return getServerRoleKeys()
            .filter(sr -> sr.roles().contains(key))
            .flatMap(sr -> webClient.build().get()
                .uri("http://" + sr.server() + "/actuator/role/" + key)
                .retrieve()
                .bodyToMono(Role.class)
                .onErrorResume(WebClientResponseException.class, e -> {
                    ProblemDetail detail = e.getResponseBodyAs(ProblemDetail.class);
                    if (detail == null) {
                        detail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                    return Mono.error(new BaseException(detail));
                }));
    }

}
