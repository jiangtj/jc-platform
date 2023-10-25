package com.jiangtj.cloud.core;

import com.jiangtj.cloud.auth.AuthKeyLocator;
import com.jiangtj.cloud.core.pk.PublicKeyService;
import io.jsonwebtoken.Header;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;

import java.security.Key;

public class CoreAuthKeyLocator implements AuthKeyLocator {

    @Lazy
    @Resource
    private PublicKeyService publicKeyService;

    @Override
    public Key locate(Header header) {
        Object kid = header.get("kid");
        return publicKeyService.getPublicKeyObject((String) kid).toKey();
    }
}
