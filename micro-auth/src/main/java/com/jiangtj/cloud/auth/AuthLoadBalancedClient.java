package com.jiangtj.cloud.auth;

import io.jsonwebtoken.security.PublicJwk;

import java.security.PublicKey;

public interface AuthLoadBalancedClient {

    PublicJwk<PublicKey> getPublicJwk(String kid);

    void notifyUpdatePublicJwk(String kid);
}
