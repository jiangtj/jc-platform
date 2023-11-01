package com.jiangtj.cloud.core.pk;

import com.jiangtj.cloud.auth.AuthExceptionUtils;
import com.jiangtj.cloud.auth.AuthRequestAttributes;
import com.jiangtj.cloud.auth.AuthServer;
import com.jiangtj.cloud.auth.TokenType;
import com.jiangtj.cloud.auth.reactive.AuthReactorHolder;
import com.jiangtj.cloud.auth.reactive.AuthReactorUtils;
import com.jiangtj.cloud.auth.sba.RoleInst;
import com.jiangtj.cloud.common.BaseExceptionUtils;
import com.jiangtj.cloud.common.utils.JsonUtils;
import io.jsonwebtoken.security.Jwks;
import io.jsonwebtoken.security.PublicJwk;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
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
    @Resource
    private PKTaskProperties pkTaskProperties;
    @Value("${spring.application.name}")
    String selfName;

    private final WebClient webClient = WebClient.create();
    private final List<MicroServiceData> serviceDataList = new ArrayList<>();
    private final Map<String, MicroServiceData> serviceDataMap = new ConcurrentHashMap<>();
    private final Map<String, MicroServiceData> linkToService = new ConcurrentHashMap<>();

    @Scheduled(initialDelayString = "${pk.task.initial-delay}", fixedDelayString = "${pk.task.delay}", timeUnit = TimeUnit.SECONDS)
    public void handlePublicKeyMap() {
        log.debug("handling public keys ...");
        for (String service : discoveryClient.getServices()) {
            if (service.equals(selfName)) {
                continue;
            }
            for (ServiceInstance instance : discoveryClient.getInstances(service)) {
                URI uri = instance.getUri();
                String serviceId = instance.getServiceId();
                MicroServiceData data = linkToService.getOrDefault(uri.toString(), null);
                Instant now = Instant.now();
                if (data == null) {
                    data = MicroServiceData.builder()
                        .server(serviceId)
                        .host(uri.getHost())
                        .uri(uri)
                        .instant(now)
                        .status(MicroServiceData.Status.Down)
                        .build();
                    serviceDataList.add(data);
                    linkToService.put(uri.toString(), data);
                }
                log.debug(JsonUtils.toJson(data));
                if (data.getStatus() == MicroServiceData.Status.Up
                    && data.getInstant().plusSeconds(pkTaskProperties.getUpDelay()).isAfter(now)) {
                    continue;
                }
                if (data.getStatus() == MicroServiceData.Status.Down
                    && data.getInstant().plusSeconds(pkTaskProperties.getDownDelay()).isAfter(now)) {
                    continue;
                }

                URI actuator = uri.resolve("/actuator/publickey");
                String header = authServer.createServerToken(serviceId);
                webClient.get().uri(actuator)
                    .header(AuthRequestAttributes.TOKEN_HEADER_NAME, header)
                    .retrieve()
                    .bodyToMono(String.class)
                    .subscribe(json -> {
                        PublicJwk<PublicKey> publicJwk = (PublicJwk<PublicKey>)Jwks.parser()
                            .build().parse(json);
                        log.error(JsonUtils.toJson(publicJwk));
                        MicroServiceData data1 = linkToService.get(uri.toString());
                        data1.setInstant(now);
                        data1.setKey(publicJwk);
                        data1.setStatus(MicroServiceData.Status.Up);
                        serviceDataMap.put(publicJwk.getId(), data1);
                    }, e -> {
                        MicroServiceData data1 = linkToService.get(uri.toString());
                        data1.setInstant(now);
                        data1.setStatus(MicroServiceData.Status.Down);
                    });
            }
        }
    }

    public Flux<MicroServiceData> getMicroServiceDatas() {
        return Flux.fromIterable(serviceDataList);
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
            .then(Mono.just(serviceDataMap.get(keyId).getKey()));
    }

    public PublicJwk<PublicKey> getPublicKeyObject(String keyId) {
        MicroServiceData data = serviceDataMap.getOrDefault(keyId, null);
        if (data == null) {
            throw BaseExceptionUtils.badRequest("无效的kid！");
        }
        return data.getKey();
    }

    public Mono<Void> updatePublicKey(UpdateDto dto) {
        String host = dto.getHost();
        for (MicroServiceData serviceData : serviceDataList){
            if (serviceData.getHost().equals(host)) {
                String kid = dto.getKid();
                String server = kid.split(":")[0];
                if (server.equals(serviceData.getServer()) && !serviceData.getKey().getId().equals(kid)) {
                    serviceData.setStatus(MicroServiceData.Status.Waiting);
                }
            }
        }
        return Mono.empty();
    }
}
