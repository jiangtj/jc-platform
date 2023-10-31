package com.jiangtj.cloud.auth.reactive;

import com.jiangtj.cloud.auth.AuthRequestAttributes;
import com.jiangtj.cloud.auth.AuthServer;
import com.jiangtj.cloud.auth.CoreInstanceService;
import com.jiangtj.cloud.common.utils.JsonUtils;
import io.jsonwebtoken.security.Jwks;
import io.jsonwebtoken.security.PublicJwk;
import jakarta.annotation.Resource;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.security.PublicKey;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static com.jiangtj.cloud.auth.reactive.ReactiveTokenFilter.ORDER;

@Order(ORDER - 10)
public class ReactivePublicKeyFilter implements WebFilter {

    /*@Resource
    @LoadBalanced
    private WebClient.Builder loadBalanced;*/

    @Resource
    private AuthServer authServer;

    @Resource
    private ReactiveCachedPublicKeyService reactiveCachedPublicKeyService;

    @Resource
    private CoreInstanceService coreInstanceService;

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

        if (reactiveCachedPublicKeyService.hasKid(kid)) {
            return chain.filter(exchange);
        }

        Optional<URI> uri = coreInstanceService.getUri();
        return uri.map(value -> WebClient.create().get().uri(value + "/service/{kid}/publickey", kid)
            .accept(MediaType.APPLICATION_JSON)
            .header(AuthRequestAttributes.TOKEN_HEADER_NAME, coreInstanceService.createToken())
            .retrieve()
            .bodyToMono(String.class)
            .flatMap(key -> {
                reactiveCachedPublicKeyService.setPublicJwk((PublicJwk<PublicKey>) Jwks.parser().build().parse(key));
                return chain.filter(exchange);
            }))
            .orElseGet(() -> chain.filter(exchange));
    }
}
