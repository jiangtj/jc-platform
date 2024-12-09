package com.jiangtj.platform.system;

import com.jiangtj.platform.spring.cloud.jwt.AuthKeyLocator;
import io.jsonwebtoken.Header;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;

import java.security.Key;

public class CoreAuthKeyLocator implements AuthKeyLocator {

    @Lazy
    @Resource
    private KeyService keyService;

    @Override
    public Key locate(Header header) {
        String kid = (String) header.get("kid");
        return keyService.getPublishKey(kid).toKey();
    }
}
