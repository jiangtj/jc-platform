package com.jiangtj.platform.spring.cloud;

import com.jiangtj.platform.common.JsonUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Jwks;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.util.Base64;

@Slf4j
class JwkHolderTest {

    @Test
    void createKeyPair() {
        KeyPair keyPair = Jwts.SIG.RS256.keyPair().build();
        log.error(Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));
        log.error(Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
    }

    @Test
    void testInit() {
        KeyPair keyPair = Jwts.SIG.RS256.keyPair().build();
        String pr = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
        String pb = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        AuthProperties properties = new AuthProperties();
        properties.setKid("test-key");
        properties.setPrivateKey(pr);
        properties.setPublicKey(pb);
        JwkHolder.init(properties, "test-service-id");
        String raw = JsonUtils.toJson(JwkHolder.getPrivateJwk());
        String expect = JsonUtils.toJson(Jwks.builder()
            .id("test-service-id:test-key")
            .keyPair(keyPair)
            .build());
        Assertions.assertEquals(expect, raw);
    }

}