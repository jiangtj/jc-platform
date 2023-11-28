package com.jiangtj.platform.auth.context;

import com.jiangtj.platform.auth.AuthExceptionUtils;
import com.jiangtj.platform.auth.TokenType;
import io.jsonwebtoken.Claims;

import java.util.List;

public class UnauthorizedContextImpl implements AuthContext {

    public static UnauthorizedContextImpl self = new UnauthorizedContextImpl();

    @Override
    public String token() {
        throw AuthExceptionUtils.unLogin();
    }

    @Override
    public Claims claims() {
        throw AuthExceptionUtils.unLogin();
    }

    @Override
    public List<String> roles() {
        throw AuthExceptionUtils.unLogin();
    }

    @Override
    public List<String> permissions() {
        throw AuthExceptionUtils.unLogin();
    }

    @Override
    public boolean isLogin() {
        return false;
    }

    @Override
    public String type() {
        return TokenType.UNAUTHORIZED;
    }

}
