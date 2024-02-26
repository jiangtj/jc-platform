package com.jiangtj.platform.core.pk;

import com.jiangtj.platform.auth.AuthExceptionUtils;
import com.jiangtj.platform.auth.AuthRequestAttributes;
import com.jiangtj.platform.auth.TokenType;
import com.jiangtj.platform.auth.context.AuthContext;
import com.jiangtj.platform.auth.servlet.AuthHolder;
import com.jiangtj.platform.auth.servlet.AuthUtils;
import com.jiangtj.platform.common.JsonUtils;
import com.jiangtj.platform.spring.cloud.AuthServer;
import com.jiangtj.platform.spring.cloud.jwt.JwtAuthContext;
import com.jiangtj.platform.spring.cloud.sba.RoleInst;
import io.jsonwebtoken.security.Jwks;
import io.jsonwebtoken.security.PublicJwk;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.net.URI;
import java.security.PublicKey;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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

    private final RestClient client = RestClient.create();
    private final List<MicroServiceData> instanceList = new ArrayList<>();
    private final Map<String, MicroServiceData> jwtIdToInstance = new ConcurrentHashMap<>();
    private final Map<String, MicroServiceData> instanceMap = new ConcurrentHashMap<>();

    // private final Sinks.Many<MicroServiceData> sink = Sinks.many().unicast().onBackpressureBuffer();

    /*@Scheduled(initialDelayString = "${pk.task.initial-delay}", fixedDelayString = "${pk.task.delay}", timeUnit = TimeUnit.SECONDS)
    public void handlePublicKeyMap() {
        log.debug("handling public keys ...");
        discoveryClient.getServices()
                .flatMap(discoveryClient::getInstances)
                .map(this::getCoreServiceInstance)
                .subscribe(this::updateCoreServiceInstance);
    }*/

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
        fetchNewServiceData(csi);
    }

    /*public void fetchPublickey(MicroServiceData csi) {
        fetchPublickeyFn(csi)
                .subscribe(null, e -> {
                    csi.setInstant(Instant.now());
                    csi.setStatus(MicroServiceData.Status.Down);
                    log.error("fetchPublickey error!");
                    log.error(JsonUtils.toJson(csi));
                });
    }*/

    public MicroServiceData fetchNewServiceData(MicroServiceData csi) {
        if (selfName.equals(csi.getServer())) {
            csi.setInstant(Instant.now());
            csi.setKey(authServer.getPrivateJwk().toPublicJwk());
            csi.setStatus(MicroServiceData.Status.Up);
            return csi;
        }
        URI actuator = csi.getUri().resolve("/actuator/publickey");
        String token = authServer.createServerToken(csi.getServer());
        // todo catch error
        try {
            String body = client.get().uri(actuator)
                .header(AuthRequestAttributes.TOKEN_HEADER_NAME, token)
                .retrieve()
                .body(String.class);
            PublicJwk<PublicKey> publicJwk = (PublicJwk<PublicKey>) Jwks.parser().build().parse(body);
            csi.setInstant(Instant.now());
            csi.setKey(publicJwk);
            csi.setStatus(MicroServiceData.Status.Up);
            jwtIdToInstance.put(publicJwk.getId(), csi);
            log.info(JsonUtils.toJson(csi));
        } catch (RestClientException e) {
            csi.setInstant(Instant.now());
            csi.setStatus(MicroServiceData.Status.Down);
            log.error("fetch new data error!");
            log.error(JsonUtils.toJson(csi));
        }
        return csi;
    }

    public List<MicroServiceData> getAllCoreServiceInstance() {
        return instanceList;
    }

    public PublicJwk<PublicKey> getPublicKey(String keyId) {
        String[] split = keyId.split(":");
        String serviceId = split[0];
        if (serviceId.equals(selfName)) {
            return authServer.getPrivateJwk().toPublicJwk();
        }
        AuthContext ctx = AuthHolder.getAuthContext();
        if (ctx instanceof JwtAuthContext jwtCtx) {
            String tokenType = jwtCtx.type();
            Set<String> audience = jwtCtx.claims().getAudience();
            if (!audience.contains(selfName)) {
                throw AuthExceptionUtils.invalidToken("不支持访问当前服务", null);
            }
            if (!TokenType.SERVER.equals(tokenType)) {
                AuthUtils.hasRole(RoleInst.ACTUATOR.name());
            }
        } else  {
            throw AuthExceptionUtils.invalidToken("不支持的 Auth Context", null);
        }

        MicroServiceData data = jwtIdToInstance.getOrDefault(keyId, null);
        if (data == null) {
            discoveryClient.getInstances(serviceId)
                .stream()
                .map(this::getCoreServiceInstance)
                .filter(csi -> {
                    Instant now = Instant.now();
                    return csi.getStatus() != MicroServiceData.Status.Up
                        || !csi.getInstant().plusSeconds(1).isAfter(now);
                })
                .forEach(this::fetchNewServiceData);
            data = jwtIdToInstance.get(keyId);
        }

        return data.getKey();
    }

    public PublicJwk<PublicKey> getPublicKeyObject(String keyId) {
        MicroServiceData data = jwtIdToInstance.getOrDefault(keyId, null);
        return data.getKey();
    }
}
