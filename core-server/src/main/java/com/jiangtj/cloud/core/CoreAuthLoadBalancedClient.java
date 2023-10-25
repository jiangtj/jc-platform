package com.jiangtj.cloud.core;

import com.jiangtj.cloud.auth.AuthLoadBalancedClient;
import com.jiangtj.cloud.auth.AuthServer;
import com.jiangtj.cloud.core.pk.PublicKeyService;
import io.jsonwebtoken.security.PublicJwk;
import jakarta.annotation.Resource;

import java.security.PublicKey;

public class CoreAuthLoadBalancedClient implements AuthLoadBalancedClient {

    @Resource
    private AuthServer authServer;
    @Resource
    private PublicKeyService publicKeyService;

    @Override
    public PublicJwk<PublicKey> getPublicJwk(String kid) {
        return publicKeyService.getPublicKeyObject(kid);
    }

    @Override
    public void notifyUpdatePublicJwk(String kid) {

    }
}
