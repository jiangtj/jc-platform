package com.jiangtj.platform.spring.cloud;

import com.jiangtj.platform.common.JsonUtils;
import com.jiangtj.platform.web.BaseExceptionUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Jwks;
import io.jsonwebtoken.security.PrivateJwk;
import io.jsonwebtoken.security.PublicJwk;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;

@Slf4j
public class JwkHolder {

    private static PrivateJwk<PrivateKey, PublicKey, ?> jwk = null;

    public static void init(AuthProperties properties, String serviceId) {
        String kid = properties.getKid();

        if (!StringUtils.hasText(kid)) {
            KeyPair keyPair = Jwts.SIG.RS256.keyPair().build();
            UUID uuid = UUID.randomUUID();
            jwk = Jwks.builder()
                .id(serviceId + ":" + uuid)
                .keyPair(keyPair)
                .build();
            log.warn("Generate new public jwk: {}", JsonUtils.toJson(jwk.toPublicJwk()));
            return;
        }

        kid = serviceId + ":" + kid;

        try {
            KeyFactory factory = KeyFactory.getInstance("RSA");
            String privateStr = properties.getPrivateKey();
            String publicStr = properties.getPublicKey();

            Objects.requireNonNull(privateStr);
            PrivateKey privateKey = factory.generatePrivate(
                new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateStr)));
            if (!StringUtils.hasText(publicStr)) {
                jwk = Jwks.builder()
                    .id(kid)
                    .key(privateKey)
                    .build();
                return;
            }

            PublicKey publicKey = factory.generatePublic(
                new X509EncodedKeySpec(Base64.getDecoder().decode(publicStr)));
            jwk = Jwks.builder()
                .id(kid)
                .key(privateKey)
                .publicKey(publicKey)
                .build();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public static PrivateJwk<PrivateKey, PublicKey, ?> getPrivateJwk() {
        if (jwk == null) {
            throw BaseExceptionUtils.internalServerError("Jwk is not init!");
        }
        return jwk;
    }

    @Nullable
    public static PublicJwk<PublicKey> getPublicJwk() {
        return getPrivateJwk().toPublicJwk();
    }
}
