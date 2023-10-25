package com.jiangtj.cloud.auth.servlet;

import com.jiangtj.cloud.auth.AuthKeyLocator;
import com.jiangtj.cloud.auth.AuthRequestAttributes;
import com.jiangtj.cloud.auth.AuthServer;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.security.Jwks;
import io.jsonwebtoken.security.PublicJwk;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.security.Key;
import java.security.PublicKey;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ServletAuthKeyLocator implements AuthKeyLocator {

    @Resource
    @LoadBalanced
    private RestTemplate loadBalanced;

    @Resource
    private ObjectProvider<AuthServer> authServers;

    private final Map<String, PublicJwk<PublicKey>> pkMap = new ConcurrentHashMap<>();


    @Override
    public Key locate(Header header) {
        String kid = String.valueOf(header.get("kid"));

        PublicJwk<PublicKey> publicJwk = pkMap.get(kid);
        if (publicJwk != null) {
            return publicJwk.toKey();
        }

        AuthServer ifUnique = authServers.getIfUnique();
        Objects.requireNonNull(ifUnique);
        String serverToken = ifUnique.createServerToken(kid.split(":")[0]);

        String url = "http://core-server/service/{kid}/publickey";
        HttpHeaders headers = new HttpHeaders();
        headers.add(AuthRequestAttributes.TOKEN_HEADER_NAME, serverToken);
        headers.add("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String json = loadBalanced.exchange(url, HttpMethod.GET, entity, String.class, kid).getBody();
        publicJwk = (PublicJwk<PublicKey>) Jwks.parser().build().parse(json);

        pkMap.put(publicJwk.getId(), publicJwk);
        return publicJwk.toKey();
    }

    @Data
    static class UpdateDto {
        private String host;
        private String kid;
    }
}
