package com.jiangtj.platform.spring.cloud.jwt;

import com.jiangtj.platform.spring.cloud.AuthServer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.jiangtj.platform.spring.cloud.jwt.JwtAuthContext.PROVIDER;

public class JwtAuthContextFactory {

    private final AuthServer authServer;
    private final Map<String, JwtAuthContextProvider> providers;

    public JwtAuthContextFactory(AuthServer authServer, List<JwtAuthContextProvider> converters) {
        this.authServer = authServer;
        this.providers = converters.stream()
            .collect(Collectors.toMap(JwtAuthContextProvider::provider, Function.identity()));
    }

    public JwtAuthContext getAuthContext(String token) {
        Claims body = authServer.verify(token).getPayload();
        String key = body.get(PROVIDER, String.class);
        if (key == null) {
            throw new JwtException("token 错误");
        }
        JwtAuthContextProvider provider = providers.get(key);
        if (provider == null) {
            throw new JwtException("token 错误");
        }
        return provider.convert(token, body);
    }

}
