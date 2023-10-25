package com.jiangtj.cloud.test;

import com.jiangtj.cloud.auth.AuthLoadBalancedClient;
import com.jiangtj.cloud.auth.AuthServer;
import io.jsonwebtoken.security.PublicJwk;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.ObjectProvider;

import java.security.PublicKey;
import java.util.Objects;

public class TestAuthLoadBalancedClient implements AuthLoadBalancedClient {

    @Resource
    ObjectProvider<AuthServer> authServers;

    @Override
    public PublicJwk<PublicKey> getPublicJwk(String kid) {
        return Objects.requireNonNull(authServers.getIfUnique()).getPrivateJwk().toPublicJwk();
    }

    @Override
    public void notifyUpdatePublicJwk(String kid) {

    }
}
