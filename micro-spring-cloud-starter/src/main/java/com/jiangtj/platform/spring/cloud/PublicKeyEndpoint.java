package com.jiangtj.platform.spring.cloud;

import io.jsonwebtoken.security.PublicJwk;
import jakarta.annotation.Resource;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;

import java.security.PublicKey;

@Endpoint(id = "publickey")
public class PublicKeyEndpoint {

    @Resource
    private AuthServer authServer;

    @ReadOperation
    public PublicJwk<PublicKey> publickey() {
        return authServer.getPrivateJwk().toPublicJwk();
    }

}
