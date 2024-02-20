package com.jiangtj.platform.spring.cloud;

import com.jiangtj.platform.auth.AuthKeyLocator;
import io.jsonwebtoken.Header;
import jakarta.annotation.Resource;

import java.security.Key;

public class ReactiveAuthKeyLocator implements AuthKeyLocator {

    @Resource
    private PublicKeyCachedService publicKeyCachedService;

    @Override
    public Key locate(Header header) {
        Object kid = header.get("kid");
        return publicKeyCachedService.getPublicJwk((String) kid).toKey();
    }
}
