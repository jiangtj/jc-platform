package com.jiangtj.cloud.auth.context;

public class UnauthorizedContextImpl implements Context {

    public static UnauthorizedContextImpl self = new UnauthorizedContextImpl();

    @Override
    public boolean isLogin() {
        return false;
    }

    @Override
    public String type() {
        return "unauthorized";
    }

}
