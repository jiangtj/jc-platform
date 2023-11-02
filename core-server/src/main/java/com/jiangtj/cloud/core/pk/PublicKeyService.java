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
import reactor.core.publisher.Sinks;

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
    private final List<MicroServiceData> instanceList = new ArrayList<>();
    private final Map<String, MicroServiceData> jwtIdToInstance = new ConcurrentHashMap<>();
    private final Map<String, MicroServiceData> instanceMap = new ConcurrentHashMap<>();

    private final Sinks.Many<MicroServiceData> sink = Sinks.many().unicast().onBackpressureBuffer();

    @Scheduled(initialDelayString = "${pk.task.initial-delay}", fixedDelayString = "${pk.task.delay}", timeUnit = TimeUnit.SECONDS)
    public void handlePublicKeyMap() {
        log.debug("handling public keys ...");
        for (String service : discoveryClient.getServices()) {
            List<ServiceInstance> instances = discoveryClient.getInstances(service);
            for (ServiceInstance si : instances) {
                MicroServiceData csi = getCoreServiceInstance(si);
                updateCoreServiceInstance(csi);
            }
        }
    }

    public MicroServiceData getCoreServiceInstance(ServiceInstance si) {
        URI uri = si.getUri();
        String serviceId = si.getServiceId();
        String instanceId = si.getInstanceId();
        MicroServiceData data = instanceMap.getOrDefault(instanceId, null);
        if (data == null) {
            data = MicroServiceData.builder()
                .server(serviceId)
                .instanceId(instanceId)
                .uri(uri)
                .instant(Instant.now())
                .status(MicroServiceData.Status.Waiting)
                .build();
            instanceList.add(data);
            instanceMap.put(instanceId, data);
        }
        if (data.getStatus() == MicroServiceData.Status.Down) {
            data.setStatus(MicroServiceData.Status.Waiting);
        }
        log.debug(JsonUtils.toJson(data));
        return data;
    }

    public void updateCoreServiceInstance(MicroServiceData csi) {
        Instant now = Instant.now();
        if (csi.getStatus() == MicroServiceData.Status.Up
            && csi.getInstant().plusSeconds(pkTaskProperties.getUpDelay()).isAfter(now)) {
            return;
        }
        fetchPublickey(csi);
    }

    public void fetchPublickey(MicroServiceData csi) {
        if (selfName.equals(csi.getServer())) {
            csi.setInstant(Instant.now());
            csi.setKey(authServer.getPrivateJwk().toPublicJwk());
            csi.setStatus(MicroServiceData.Status.Up);
            return;
        }
        URI actuator = csi.getUri().resolve("/actuator/publickey");
        String header = authServer.createServerToken(csi.getServer());
        webClient.get().uri(actuator)
            .header(AuthRequestAttributes.TOKEN_HEADER_NAME, header)
            .retrieve()
            .bodyToMono(String.class)
            .subscribe(json -> {
                PublicJwk<PublicKey> publicJwk = (PublicJwk<PublicKey>)Jwks.parser()
                    .build().parse(json);
                log.error(JsonUtils.toJson(publicJwk));
                csi.setInstant(Instant.now());
                csi.setKey(publicJwk);
                csi.setStatus(MicroServiceData.Status.Up);
                jwtIdToInstance.put(publicJwk.getId(), csi);
            }, e -> {
                csi.setInstant(Instant.now());
                csi.setStatus(MicroServiceData.Status.Down);
            });
    }

    public List<MicroServiceData> getAllCoreServiceInstance() {
        return instanceList;
    }

    public Flux<MicroServiceData> getMicroServiceDatas() {
        return Flux.fromIterable(instanceList);
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
            .then(Mono.just(jwtIdToInstance.get(keyId).getKey()));
    }

    public PublicJwk<PublicKey> getPublicKeyObject(String keyId) {
        MicroServiceData data = jwtIdToInstance.getOrDefault(keyId, null);
        if (data == null) {
            throw BaseExceptionUtils.badRequest("无效的kid！");
        }
        return data.getKey();
    }
}
