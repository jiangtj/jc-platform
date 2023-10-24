package com.jiangtj.cloud.auth.servlet;

import com.jiangtj.cloud.auth.AuthLoadBalancedClient;
import io.jsonwebtoken.security.Jwks;
import io.jsonwebtoken.security.PublicJwk;
import jakarta.annotation.Resource;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.web.client.RestTemplate;

import java.security.PublicKey;

public class ServletAuthLoadBalancedClient implements AuthLoadBalancedClient {

    @Resource
    @LoadBalanced
    private RestTemplate loadBalanced;

    @Override
    public PublicJwk<PublicKey> getPublicJwk(String kid) {
        String json = loadBalanced.getForObject("http://token-server//service/{kid}/publickey", String.class, kid);
        return (PublicJwk<PublicKey>) Jwks.parser().build().parse(json);
    }
}
