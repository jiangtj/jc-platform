package com.jiangtj.platform.system;

import com.jiangtj.platform.spring.cloud.JwkHolder;
import com.jiangtj.platform.system.dto.SharePublicKey;
import com.jiangtj.platform.test.cloud.JMicroCloudMvcTest;
import com.jiangtj.platform.web.ApplicationProperty;
import io.jsonwebtoken.security.PublicJwk;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.security.PublicKey;

@Slf4j
@JMicroCloudMvcTest
class KeyServiceTest {

    @Resource
    private KeyService keyService;
    @Resource
    private ApplicationProperty applicationProperty;

    @Test
    void publishKey() {
        PublicJwk<PublicKey> publicJwk = JwkHolder.getPublicJwk();
        SharePublicKey key = new SharePublicKey();
        key.setApplication(applicationProperty.getName());
        key.setJwk(publicJwk);
        keyService.publishKey(key);
    }

    @Test
    void getPublishKey() {
        PublicJwk<PublicKey> publicJwk = JwkHolder.getPublicJwk();
        publishKey();
        PublicJwk<PublicKey> k2 = keyService.getPublishKey(publicJwk.getId());
        log.info("k2: {}", k2);
    }
}