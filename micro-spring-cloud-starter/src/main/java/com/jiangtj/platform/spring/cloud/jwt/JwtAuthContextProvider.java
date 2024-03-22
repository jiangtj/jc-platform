package com.jiangtj.platform.spring.cloud.jwt;

import io.jsonwebtoken.Claims;

import java.util.function.BiFunction;

public interface JwtAuthContextProvider {

    String provider();

    JwtAuthContext convert(String token, Claims body);

    static JwtAuthContextProvider create(String provider, BiFunction<String, Claims, JwtAuthContext> bf) {
        return new JwtAuthContextProvider() {
            @Override
            public String provider() {
                return provider;
            }

            @Override
            public JwtAuthContext convert(String token, Claims body) {
                return bf.apply(token, body);
            }
        };
    }

}
