package com.jtj.cloud.auth.context;

import io.jsonwebtoken.Claims;

import java.util.function.BiFunction;

public interface AuthContextConverter {

    String type();

    AuthContext convert(String token, Claims body);


    static AuthContextConverter register(String type, BiFunction<String, Claims, AuthContext> bf) {
        return new AuthContextConverter() {
            @Override
            public String type() {
                return type;
            }

            @Override
            public AuthContext convert(String token, Claims body) {
                return bf.apply(token, body);
            }
        };
    }
}
