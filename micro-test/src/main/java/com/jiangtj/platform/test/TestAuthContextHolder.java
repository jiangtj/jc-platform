package com.jiangtj.platform.test;

import com.jiangtj.platform.auth.context.AuthContext;

public class TestAuthContextHolder {

    private static AuthContext ctx = AuthContext.unauthorized();

    public static void setAuthContext(AuthContext context) {
        ctx = context;
    }

    public static AuthContext getAuthContext() {
        return ctx;
    }
}
