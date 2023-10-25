package com.jiangtj.cloud.auth.reactive;

import com.jiangtj.cloud.auth.AuthKeyLocator;
import io.jsonwebtoken.Header;
import jakarta.annotation.Resource;

import java.security.Key;

public class ReactiveAuthKeyLocator implements AuthKeyLocator {

    @Resource
    private ReactiveCachedPublicKeyService cachedPublicKeyService;

    @Override
    public Key locate(Header header) {
        Object kid = header.get("kid");
        return cachedPublicKeyService.getPublicJwk((String) kid).toKey();
    }
}
