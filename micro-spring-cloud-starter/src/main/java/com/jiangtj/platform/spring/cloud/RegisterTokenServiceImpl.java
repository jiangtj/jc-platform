package com.jiangtj.platform.spring.cloud;

import com.jiangtj.platform.spring.cloud.core.CoreInstanceApi;
import com.jiangtj.platform.spring.cloud.core.RegisterPublicKey;
import com.jiangtj.platform.spring.cloud.core.RegisterTokenService;
import com.jiangtj.platform.web.ApplicationProperty;
import io.jsonwebtoken.security.Jwks;
import io.jsonwebtoken.security.PublicJwk;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.security.PublicKey;
import java.util.List;
import java.util.Objects;

@Slf4j
public class RegisterTokenServiceImpl extends InstanceDiscoveryListener implements RegisterTokenService {

    private boolean isR = false;
    private final CoreInstanceApi coreInstanceApi;
    @Resource
    private DiscoveryClient discoveryClient;
    @Resource
    private ApplicationProperty applicationProperty;

    public RegisterTokenServiceImpl(CoreInstanceApi coreInstanceApi) {
        this.coreInstanceApi = coreInstanceApi;
    }

    public void register() {
        /*String adminToken = coreInstanceApi.getAdminToken();
        PublicJwk<PublicKey> adminJwk = (PublicJwk<PublicKey>) Jwks.parser().build().parse(adminToken);
        adminJwk.toKey();*/
        // todo 加密传输
        PublicJwk<PublicKey> jwk = JwkHolder.getPublicJwk();
        RegisterPublicKey register = new RegisterPublicKey();
        register.setJwk(Jwks.json(jwk));
        register.setApplication(Objects.requireNonNull(applicationProperty.getName()));
        coreInstanceApi.registerToken(Objects.requireNonNull(jwk).getId(), register);
    }

    @Override
    public void discover() {
        if (isR) return;
        log.error("discovering client change.");
        List<String> services = discoveryClient.getServices();
        if (!services.contains("system-server")) {
            log.error("fail found system-server.");
            return;
        }
        int size = discoveryClient.getInstances("system-server").size();
        if (size == 0) {
            log.error("No instances available for system-server.");
            return;
        }
        log.error("start register.");
        register();
        isR = true;
    }
}
