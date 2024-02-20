package com.jiangtj.platform.spring.cloud;

import com.jiangtj.platform.auth.TokenType;
import com.jiangtj.platform.spring.cloud.jwt.JwtAuthContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JwtAuthContextFactory {

    private final AuthServer authServer;
    private final Map<String, JwtAuthContextConverter> typeToConverter;

    public JwtAuthContextFactory(AuthServer authServer, List<JwtAuthContextConverter> converters) {
        this.authServer = authServer;
        this.typeToConverter = converters.stream()
            .collect(Collectors.toMap(JwtAuthContextConverter::type, Function.identity()));
    }

    public JwtAuthContext getAuthContext(String token) {
        Claims body = authServer.verify(token).getPayload();
        String type = body.get(TokenType.KEY, String.class);
        if (type == null) {
            throw new JwtException("token 错误");
        }
        JwtAuthContextConverter authContextConverter = typeToConverter.get(type);
        if (authContextConverter == null) {
            throw new JwtException("token 错误");
        }
        return authContextConverter.convert(token, body);
    }

}
