package com.jiangtj.platform.system;

import com.jiangtj.platform.spring.cloud.JwkHolder;
import com.jiangtj.platform.spring.cloud.PublicKeyCachedService;
import com.jiangtj.platform.system.entity.SharePublicKey;
import com.jiangtj.platform.test.cloud.JMicroCloudMvcTest;
import io.jsonwebtoken.security.Jwks;
import io.jsonwebtoken.security.PublicJwk;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@JMicroCloudMvcTest
class KeyServiceTest {

    @Resource
    private KeyService keyService;

    @Test
    void publishKey() {
        PublicJwk<PublicKey> publicJwk = JwkHolder.getPublicJwk();
        SharePublicKey key = new SharePublicKey();
        key.setKid(Objects.requireNonNull(publicJwk).getId());
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