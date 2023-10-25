package com.jiangtj.cloud.auth.context;

import com.jiangtj.cloud.auth.AuthServer;
import com.jiangtj.cloud.auth.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AuthContextFactory {

    private final AuthServer authServer;
    private final Map<String, AuthContextConverter> typeToConverter;

    public AuthContextFactory(AuthServer authServer, List<AuthContextConverter> converters) {
        this.authServer = authServer;
        this.typeToConverter = converters.stream()
            .collect(Collectors.toMap(AuthContextConverter::type, Function.identity()));
    }

    public AuthContext getAuthContext(String token) {
        Claims body = authServer.verify(token).getPayload();
        String type = body.get(TokenType.KEY, String.class);
        if (type == null) {
            throw new JwtException("token 错误");
        }
        AuthContextConverter authContextConverter = typeToConverter.get(type);
        if (authContextConverter == null) {
            throw new JwtException("token 错误");
        }
        return authContextConverter.convert(token, body);
    }

}
