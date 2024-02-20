package com.jiangtj.platform.test.cloud;

import com.jiangtj.platform.spring.cloud.AuthServer;
import com.jiangtj.platform.spring.cloud.jwt.AuthKeyLocator;
import io.jsonwebtoken.Header;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.ObjectProvider;

import java.security.Key;
import java.util.Objects;

public class TestAuthKeyLocator implements AuthKeyLocator {

    @Resource
    ObjectProvider<AuthServer> authServers;

    @Override
    public Key locate(Header header) {
        return Objects.requireNonNull(authServers.getIfUnique())
            .getPrivateJwk()
            .toPublicJwk()
            .toKey();
    }
}
