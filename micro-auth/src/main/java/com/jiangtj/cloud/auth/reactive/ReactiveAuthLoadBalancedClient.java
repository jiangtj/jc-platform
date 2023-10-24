package com.jiangtj.cloud.auth.reactive;

import com.jiangtj.cloud.auth.AuthLoadBalancedClient;
import io.jsonwebtoken.security.Jwks;
import io.jsonwebtoken.security.PublicJwk;
import jakarta.annotation.Resource;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.web.reactive.function.client.WebClient;

import java.security.PublicKey;
import java.util.concurrent.ExecutionException;

public class ReactiveAuthLoadBalancedClient implements AuthLoadBalancedClient {

    @Resource
    @LoadBalanced
    private WebClient.Builder loadBalanced;

    @Override
    public PublicJwk<PublicKey> getPublicJwk(String kid) {
        try {
            String json = loadBalanced.build().get().uri("http://token-server//service/{kid}/publickey", kid)
                .retrieve()
                .bodyToMono(String.class)
                .toFuture()
                .get();
            return (PublicJwk<PublicKey>) Jwks.parser().build().parse(json);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
