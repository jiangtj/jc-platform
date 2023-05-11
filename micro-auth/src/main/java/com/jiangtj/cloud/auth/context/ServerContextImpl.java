package com.jiangtj.cloud.auth.context;

import io.jsonwebtoken.Claims;

public class ServerContextImpl implements AuthContext {
    private final String token;
    private final Claims claims;

    public ServerContextImpl(String token, Claims claims) {
        this.token = token;
        this.claims = claims;
    }

    @Override
    public boolean isLogin() {
        return true;
    }

    @Override
    public String token() {
        return token;
    }

    @Override
    public Claims claims() {
        return claims;
    }

}
