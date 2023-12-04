package com.jiangtj.platform.auth.cloud;

import io.jsonwebtoken.security.PublicJwk;

import java.security.PublicKey;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PublicKeyCachedService {

    private final Map<String, PublicJwk<PublicKey>> pkMap = new ConcurrentHashMap<>();

    public PublicJwk<PublicKey> getPublicJwk(String kid) {
        return pkMap.get(kid);
    }

    public boolean hasKid(String kid) {
        return pkMap.containsKey(kid);
    }

    public void setPublicJwk(PublicJwk<PublicKey> jwk) {
        pkMap.put(jwk.getId(), jwk);
    }
}
