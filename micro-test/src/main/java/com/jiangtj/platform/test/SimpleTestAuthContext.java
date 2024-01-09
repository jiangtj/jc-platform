package com.jiangtj.platform.test;

import com.jiangtj.platform.auth.context.AuthContext;

import java.util.List;

public class SimpleTestAuthContext implements AuthContext {

    private final String subject;
    private final List<String> roles;
    private final List<String> permissions;

    public SimpleTestAuthContext(String subject, List<String> roles, List<String> permissions) {
        this.subject = subject;
        this.roles = roles;
        this.permissions = permissions;
    }

    @Override
    public boolean isLogin() {
        return true;
    }

    @Override
    public String subject() {
        return subject;
    }

    @Override
    public List<String> roles() {
        return roles;
    }

    @Override
    public List<String> permissions() {
        return permissions;
    }
}
