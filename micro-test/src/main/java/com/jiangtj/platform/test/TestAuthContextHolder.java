package com.jiangtj.platform.test;

import com.jiangtj.platform.auth.context.AuthContext;
import com.jiangtj.platform.auth.context.RoleProvider;

public class TestAuthContextHolder {

    private static AuthContext ctx = AuthContext.unauthorized();
    private static RoleProvider provider = null;

    public static void setAuthContext(AuthContext context) {
        ctx = context;
    }

    public static AuthContext getAuthContext() {
        return ctx;
    }

    public static void clearAuthContext() {
        ctx = AuthContext.unauthorized();
    }

    public static RoleProvider getProvider() {
        return provider;
    }

    public static void setProvider(RoleProvider provider) {
        TestAuthContextHolder.provider = provider;
    }

    public static void clearProvider() {
        provider = null;
    }
}
