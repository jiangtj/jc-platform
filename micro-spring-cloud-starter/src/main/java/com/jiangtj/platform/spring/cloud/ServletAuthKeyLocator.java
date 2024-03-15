package com.jiangtj.platform.spring.cloud;

import com.jiangtj.platform.spring.cloud.core.CoreInstanceApi;
import com.jiangtj.platform.spring.cloud.jwt.AuthKeyLocator;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.security.Jwks;
import io.jsonwebtoken.security.PublicJwk;
import jakarta.annotation.Resource;
import org.springframework.web.client.RestTemplate;

import java.security.Key;
import java.security.PublicKey;

public class ServletAuthKeyLocator implements AuthKeyLocator {

    private final RestTemplate restTemplate = new RestTemplate();
    @Resource
    private CoreInstanceService coreInstanceService;
    @Resource
    private PublicKeyCachedService publicKeyCachedService;
    @Resource
    private CoreInstanceApi coreInstanceApi;


    @Override
    public Key locate(Header header) {
        String kid = String.valueOf(header.get("kid"));

        if (publicKeyCachedService.hasKid(kid)) {
            return publicKeyCachedService.getPublicJwk(kid).toKey();
        }

        String json = coreInstanceApi.getToken(kid);
        PublicJwk<PublicKey> publicJwk = (PublicJwk<PublicKey>) Jwks.parser().build().parse(json);

        publicKeyCachedService.setPublicJwk(publicJwk);
        return publicJwk.toKey();
    }
}
