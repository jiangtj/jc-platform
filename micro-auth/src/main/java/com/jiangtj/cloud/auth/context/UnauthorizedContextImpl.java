package com.jiangtj.cloud.auth.context;

import com.jiangtj.cloud.auth.AuthExceptionUtils;
import com.jiangtj.cloud.auth.rbac.RoleProvider;
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
    public RoleProvider roleProvider() {
        throw AuthExceptionUtils.unLogin();
    }

    @Override
    public boolean isLogin() {
        return false;
    }

    @Override
    public String type() {
        throw AuthExceptionUtils.unLogin();
    }

}
