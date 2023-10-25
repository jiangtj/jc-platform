package com.jiangtj.cloud.auth.servlet;

import com.jiangtj.cloud.auth.AuthLoadBalancedClient;
import com.jiangtj.cloud.auth.AuthRequestAttributes;
import com.jiangtj.cloud.auth.AuthServer;
import io.jsonwebtoken.security.Jwks;
import io.jsonwebtoken.security.PublicJwk;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.security.PublicKey;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class ServletAuthLoadBalancedClient implements AuthLoadBalancedClient {

    @Resource
    @LoadBalanced
    private RestTemplate loadBalanced;

    @Resource
    private ObjectProvider<AuthServer> authServers;

    @Resource
    private InetUtils inetUtils;

    @Override
    public PublicJwk<PublicKey> getPublicJwk(String kid) {
        AuthServer ifUnique = authServers.getIfUnique();
        Objects.requireNonNull(ifUnique);
        String serverToken = ifUnique.createServerToken(kid.split(":")[0]);

        String url = "http://core-server/service/{kid}/publickey";
        HttpHeaders headers = new HttpHeaders();
        headers.add(AuthRequestAttributes.TOKEN_HEADER_NAME, serverToken);
        headers.add("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String json = loadBalanced.exchange(url, HttpMethod.GET, entity, String.class, kid).getBody();

        //String json = loadBalanced.getForObject("http://core-server/service/{kid}/publickey", String.class, kid);
        return (PublicJwk<PublicKey>) Jwks.parser().build().parse(json);
    }

    @Override
    public void notifyUpdatePublicJwk(String kid) {
        String address = inetUtils.findFirstNonLoopbackHostInfo().getIpAddress();
        UpdateDto updateDto = new UpdateDto();
        updateDto.setHost(address);
        updateDto.setKid(kid);
        HttpEntity<UpdateDto> entity = new HttpEntity<>(updateDto);
        CompletableFuture.delayedExecutor(15, TimeUnit.SECONDS).execute(() -> {
            loadBalanced.put("http://core-server/service/publickey", entity);
        });
    }

    @Data
    static class UpdateDto {
        private String host;
        private String kid;
    }
}
