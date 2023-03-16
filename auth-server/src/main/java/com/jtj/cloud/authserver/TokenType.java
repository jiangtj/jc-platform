package com.jtj.cloud.authserver;

import io.jsonwebtoken.Claims;

public enum TokenType {
    SERVER, SYSTEM, USER, UNKNOWN;

    public static TokenType from(Claims body) {
        String name = body.get(TokenProperties.TYPE, String.class);
        if (name == null) {
            return UNKNOWN;
        }
        return TokenType.valueOf(name);
    }
}
