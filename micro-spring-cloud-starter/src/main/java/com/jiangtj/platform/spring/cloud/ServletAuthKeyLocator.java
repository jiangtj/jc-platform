package com.jiangtj.platform.spring.cloud;

import com.jiangtj.platform.spring.cloud.core.CoreInstanceApi;
import com.jiangtj.platform.spring.cloud.jwt.AuthKeyLocator;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.security.Jwks;
import io.jsonwebtoken.security.PublicJwk;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.security.PublicKey;

@Slf4j
public class ServletAuthKeyLocator implements AuthKeyLocator {

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

        String responseBody = coreInstanceApi.getToken(kid);
        log.debug("call core instance api and fetch kid: " + kid);
        log.debug(responseBody);
        PublicJwk<PublicKey> publicJwk = (PublicJwk<PublicKey>) Jwks.parser().build().parse(responseBody);

        publicKeyCachedService.setPublicJwk(publicJwk);
        return publicJwk.toKey();
    }
}
