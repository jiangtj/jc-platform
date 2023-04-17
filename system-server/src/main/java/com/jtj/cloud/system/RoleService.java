package com.jtj.cloud.system;

import com.jtj.cloud.auth.AuthServer;
import com.jtj.cloud.auth.RequestAttributes;
import com.jtj.cloud.auth.TokenType;
import com.jtj.cloud.auth.rbac.RoleEndpoint;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
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

    @Scheduled(initialDelay = 0, fixedDelay = 60, timeUnit = TimeUnit.SECONDS)
    public void fetchAndCacheRole() {
        serverRoles = Flux.fromIterable(discoveryClient.getServices())
            .flatMap(s -> {
                if (selfName.equals(s)) {
                    return Mono.just(roleEndpoint.roles())
                        .map(r -> new ServerRole(s, r));
                }

                String token = authServer.builder()
                    .setAudience(s)
                    .setAuthType(TokenType.SERVER)
                    .build();

                return webClient.build().get()
                    .uri("http://" + s + "/actuator/role")
                    .headers(httpHeaders -> {
                        httpHeaders.remove(RequestAttributes.TOKEN_HEADER_NAME);
                        httpHeaders.add(RequestAttributes.TOKEN_HEADER_NAME, token);
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
            .block();
    }

    public Flux<ServerRole> getRole() {
        return Flux.fromIterable(serverRoles);
    }

    record ServerRole(String server, List<String> roles) {}

}
