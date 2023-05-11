package com.jiangtj.cloud.auth.context;

public interface Context {
    boolean isLogin();
    String type();

    static Context unauthorized() {
        return UnauthorizedContextImpl.self;
    }
}
