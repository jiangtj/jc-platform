package com.jiangtj.platform.auth.context;

import com.jiangtj.platform.auth.AuthExceptionUtils;

import java.util.List;

public class UnLoginContextImpl implements AuthContext {

    public static UnLoginContextImpl self = new UnLoginContextImpl();

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
    public String subject() {
        throw AuthExceptionUtils.unLogin();
    }

}
