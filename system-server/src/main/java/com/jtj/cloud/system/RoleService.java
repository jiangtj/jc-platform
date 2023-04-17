package com.jtj.cloud.system;

import com.jtj.cloud.auth.rbac.RoleEndpoint;
import com.jtj.cloud.common.JsonUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

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

    public Flux<List<String>> getRole() {
        List<String> services = discoveryClient.getServices();
        log.error(JsonUtil.toJson(services));
        return Flux.fromIterable(services)
            .flatMap(s -> {
                if (selfName.equals(s)) {
                    return Mono.just(roleEndpoint.roles());
                }
                return webClient.build().get().uri("http://" + s + "/actuator/role")
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                    .onErrorResume(WebClientResponseException.class, e -> {
                        log.error("call actuator role error", e);
                        return Mono.just(Collections.emptyList());
                    });
            });
    }

}
