package com.jiangtj.platform.spring.cloud.jwt;

import io.jsonwebtoken.Claims;

import java.util.function.BiFunction;

public interface JwtAuthContextConverter {

    String type();

    JwtAuthContext convert(String token, Claims body);


    static JwtAuthContextConverter register(String type, BiFunction<String, Claims, JwtAuthContext> bf) {
        return new JwtAuthContextConverter() {
            @Override
            public String type() {
                return type;
            }

            @Override
            public JwtAuthContext convert(String token, Claims body) {
                return bf.apply(token, body);
            }
        };
    }

}
