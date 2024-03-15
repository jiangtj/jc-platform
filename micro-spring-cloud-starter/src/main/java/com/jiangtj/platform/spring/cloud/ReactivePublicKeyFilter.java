package com.jiangtj.platform.spring.cloud;

import com.jiangtj.platform.auth.AuthRequestAttributes;
import com.jiangtj.platform.auth.reactive.JwtHeader;
import com.jiangtj.platform.common.JsonUtils;
import com.jiangtj.platform.spring.cloud.core.ReactiveCoreInstanceApi;
import io.jsonwebtoken.security.Jwks;
import io.jsonwebtoken.security.PublicJwk;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.security.PublicKey;
import java.util.Base64;
import java.util.List;

@Slf4j
@Order(-200)
public class ReactivePublicKeyFilter implements WebFilter {

    @Resource
    private PublicKeyCachedService publicKeyCachedService;

    @Resource
    private ReactiveCoreInstanceApi reactiveCoreInstanceApi;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (request.getMethod() == HttpMethod.OPTIONS) {
            return chain.filter(exchange);
        }

        List<String> headers = request.getHeaders().get(AuthRequestAttributes.TOKEN_HEADER_NAME);
        if (headers == null || headers.size() != 1) {
            return chain.filter(exchange);
        }

        String token = headers.get(0);
        String[] split = token.split("\\.");
        String jsonHeader = split[0].substring(AuthRequestAttributes.TOKEN_HEADER_PREFIX.length());
        byte[] decode = Base64.getDecoder().decode(jsonHeader.getBytes());
        String jwtHeaderJson = new String(decode);
        JwtHeader jwtHeader = JsonUtils.fromJson(jwtHeaderJson, JwtHeader.class);
        String kid = jwtHeader.getKid();

        if (publicKeyCachedService.hasKid(kid)) {
            return chain.filter(exchange);
        }

        return reactiveCoreInstanceApi.getToken(kid)
            .flatMap(responseBody -> {
                log.debug("call core instance api and fetch kid: " + kid);
                log.debug(responseBody);
                publicKeyCachedService.setPublicJwk((PublicJwk<PublicKey>) Jwks.parser().build().parse(responseBody));
                return chain.filter(exchange);
            });
    }
}
