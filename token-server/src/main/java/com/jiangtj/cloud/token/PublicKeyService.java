package com.jiangtj.cloud.token;

import com.jiangtj.cloud.auth.AuthExceptionUtils;
import com.jiangtj.cloud.auth.AuthRequestAttributes;
import com.jiangtj.cloud.auth.AuthServer;
import com.jiangtj.cloud.auth.TokenType;
import com.jiangtj.cloud.auth.reactive.AuthReactorHolder;
import com.jiangtj.cloud.auth.reactive.AuthReactorUtils;
import com.jiangtj.cloud.auth.sba.RoleInst;
import com.jiangtj.cloud.common.utils.JsonUtils;
import io.jsonwebtoken.security.Jwks;
import io.jsonwebtoken.security.PublicJwk;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.security.PublicKey;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class PublicKeyService {

    @Resource
    private AuthServer authServer;
    @Resource
    private DiscoveryClient discoveryClient;
    @Value("${spring.application.name}")
    String selfName;
    private final Map<String, PublicJwk<PublicKey>> publicKeyMap = new ConcurrentHashMap<>();

    private final WebClient webClient = WebClient.create();
    private final List<MicroServiceData> serviceDataList = new ArrayList<>();

    @Scheduled(initialDelay = 0, fixedDelay = 10, timeUnit = TimeUnit.SECONDS)
    public void handlePublicKeyMap() {
        log.error("handling public keys ...");
        List<URI> uris = serviceDataList.stream().map(MicroServiceData::getUri).toList();
        Flux.fromIterable(discoveryClient.getServices())
            .filter(s -> !selfName.equals(s))
            .flatMapIterable(s -> discoveryClient.getInstances(s))
            .flatMap(instance -> {
                URI uri = instance.getUri();
                log.error(uri.toString());
                if (uris.contains(uri)) {
                    return Mono.empty();
                }
                URI actuator = uri.resolve("/actuator/publickey");
                String header = authServer.builder()
                    .setAudience(instance.getServiceId())
                    .setAuthType(TokenType.SERVER)
                    .build();
                return webClient.get().uri(actuator)
                    .header(AuthRequestAttributes.TOKEN_HEADER_NAME, header)
                    .retrieve()
                    .bodyToMono(String.class)
                    .doOnNext(jsonKey -> {
                        PublicJwk<PublicKey> publicJwk = (PublicJwk<PublicKey>)Jwks.parser()
                            .build().parse(jsonKey);
                        log.error(JsonUtils.toJson(publicJwk));
                        String id = publicJwk.getId();
                        publicKeyMap.put(id, publicJwk);
                        serviceDataList.add(MicroServiceData.builder()
                            .uri(uri)
                            .key(publicJwk)
                            .instant(Instant.now())
                            .build());
                    });
            })
            .subscribe();
    }

    public Flux<PublicJwk<PublicKey>> getPublicJwks() {
        return Flux.fromIterable(serviceDataList)
            .map(MicroServiceData::getKey);
    }

    public Mono<PublicJwk<PublicKey>> getPublicKey(String keyId) {
        if (keyId.startsWith(selfName + ":")) {
            return Mono.just(authServer.getPrivateJwk().toPublicJwk());
        }
        return AuthReactorHolder.deferAuthContext()
            .flatMap(ctx -> {
                String tokenType = ctx.type();
                Set<String> audience = ctx.claims().getAudience();
                if (!audience.contains(selfName)) {
                    return Mono.error(AuthExceptionUtils.invalidToken("不支持访问当前服务", null));
                }
                if (!TokenType.SERVER.equals(tokenType)) {
                    return Mono.just(ctx).flatMap(AuthReactorUtils.hasRoleHandler(RoleInst.ACTUATOR.name()));
                }
                return Mono.just(ctx);
            })
            .then(Mono.just(publicKeyMap.get(keyId)));
    }
}
