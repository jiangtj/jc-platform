package com.jiangtj.platform.core.pk;

import com.jiangtj.platform.auth.AuthExceptionUtils;
import com.jiangtj.platform.auth.AuthRequestAttributes;
import com.jiangtj.platform.auth.context.AuthContext;
import com.jiangtj.platform.auth.servlet.AuthHolder;
import com.jiangtj.platform.auth.servlet.AuthUtils;
import com.jiangtj.platform.common.JsonUtils;
import com.jiangtj.platform.spring.cloud.AuthServer;
import com.jiangtj.platform.spring.cloud.Providers;
import com.jiangtj.platform.spring.cloud.jwt.JwtAuthContext;
import com.jiangtj.platform.spring.cloud.sba.RoleInst;
import com.jiangtj.platform.web.BaseExceptionUtils;
import io.jsonwebtoken.security.Jwks;
import io.jsonwebtoken.security.PublicJwk;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
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
    @Lazy
    @Resource
    private PublicKeyService publicKeyService;
    @Value("${spring.application.name}")
    String selfName;

    private final RestClient client = RestClient.create();
    private final List<MicroServiceData> instanceList = new ArrayList<>();
    private final Map<String, MicroServiceData> jwtIdToInstance = new ConcurrentHashMap<>();
    private final Map<String, MicroServiceData> instanceMap = new ConcurrentHashMap<>();

    @Scheduled(initialDelayString = "${pk.task.initial-delay}", fixedDelayString = "${pk.task.delay}", timeUnit = TimeUnit.SECONDS)
    public void handlePublicKeyMap() {
        log.debug("handling public keys ...");
        publicKeyService.fetchNewServiceDatas();
    }

    public void createCoreServiceInstance(ServiceInstance si) {
        String instanceId = si.getInstanceId();
        MicroServiceData data = instanceMap.get(instanceId);
        if (data == null) {
            data = MicroServiceData.builder()
                .server(si.getServiceId())
                .instanceId(instanceId)
                .uri(si.getUri())
                .instant(Instant.now())
                .status(MicroServiceData.Status.Waiting)
                .build();
            instanceList.add(data);
            instanceMap.put(instanceId, data);
        }
        if (data.getStatus() == MicroServiceData.Status.Down) {
            data.setStatus(MicroServiceData.Status.Waiting);
        }
    }

    @Async
    public void fetchNewServiceDatas() {
        instanceList.forEach(csi -> {
            Instant now = Instant.now();
            if (csi.getStatus() == MicroServiceData.Status.Up
                && csi.getInstant().plusSeconds(pkTaskProperties.getUpDelay()).isAfter(now)) {
                return;
            }
            if (csi.getStatus() == MicroServiceData.Status.Down) {
                return;
            }
            fetchNewServiceData(csi);
        });
    }

    @Async
    public void fetchNewServiceDatasByServiceId(String serviceId) {
        instanceList.forEach(csi -> {
            if (csi.getServer().equals(serviceId))
                fetchNewServiceData(csi);
        });
    }

    public void fetchNewServiceData(MicroServiceData csi) {
        if (selfName.equals(csi.getServer())) {
            csi.setInstant(Instant.now());
            csi.setKey(authServer.getPrivateJwk().toPublicJwk());
            csi.setStatus(MicroServiceData.Status.Up);
            return;
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
            csi.setStatus(MicroServiceData.Status.Fail);
            log.error("fetch new data error!");
            log.error(JsonUtils.toJson(csi));
        }
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
            String provider = jwtCtx.provider();
            Set<String> audience = jwtCtx.claims().getAudience();
            if (!audience.contains(selfName)) {
                throw AuthExceptionUtils.invalidToken("不支持访问当前服务", null);
            }
            if (!Providers.SERVER.equals(provider)) {
                AuthUtils.hasRole(RoleInst.ACTUATOR.name());
            }
        } else  {
            throw AuthExceptionUtils.invalidToken("不支持的 Auth Context", null);
        }

        MicroServiceData data = jwtIdToInstance.getOrDefault(keyId, null);
        if (data == null) {
            publicKeyService.fetchNewServiceDatasByServiceId(serviceId);
            throw BaseExceptionUtils.badRequest("当前 Key 不可用，请稍后再试");
        }
        return data.getKey();
    }

    public PublicJwk<PublicKey> getPublicKeyObject(String keyId) {
        MicroServiceData data = jwtIdToInstance.getOrDefault(keyId, null);
        return data.getKey();
    }
}
