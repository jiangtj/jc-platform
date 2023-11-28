package com.jiangtj.platform.auth.servlet;

import com.jiangtj.platform.auth.AuthKeyLocator;
import com.jiangtj.platform.auth.AuthRequestAttributes;
import com.jiangtj.platform.auth.CoreInstanceService;
import com.jiangtj.platform.auth.PublicKeyCachedService;
import com.jiangtj.platform.common.BaseExceptionUtils;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.security.Jwks;
import io.jsonwebtoken.security.PublicJwk;
import jakarta.annotation.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.security.Key;
import java.security.PublicKey;

public class ServletAuthKeyLocator implements AuthKeyLocator {

    private final RestTemplate restTemplate = new RestTemplate();
    @Resource
    private CoreInstanceService coreInstanceService;
    @Resource
    private PublicKeyCachedService publicKeyCachedService;


    @Override
    public Key locate(Header header) {
        String kid = String.valueOf(header.get("kid"));

        if (publicKeyCachedService.hasKid(kid)) {
            return publicKeyCachedService.getPublicJwk(kid).toKey();
        }

        URI uri = coreInstanceService.getUri().orElseThrow(() ->
            BaseExceptionUtils.internalServerError("Core Server isn't running!"));

        String url = String.format("%s/service/%s/publickey", uri, kid);
        HttpHeaders headers = new HttpHeaders();
        headers.add(AuthRequestAttributes.TOKEN_HEADER_NAME, coreInstanceService.createToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("charset", "utf-8");
        HttpEntity<Object> entity = new HttpEntity<>(null, headers);
        String json = restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
//        String json = loadBalanced.getForObject(url, String.class);
        PublicJwk<PublicKey> publicJwk = (PublicJwk<PublicKey>) Jwks.parser().build().parse(json);

        publicKeyCachedService.setPublicJwk(publicJwk);
        return publicJwk.toKey();
    }
}
