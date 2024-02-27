package com.jiangtj.platform.auth.context;

import java.util.List;

/**
 * Auth 上下文
 */
public interface AuthContext {

    boolean isLogin();

    String subject();

    List<String> roles();

    List<String> permissions();

    static AuthContext unLogin() {
        return UnLoginContextImpl.self;
    }
}
