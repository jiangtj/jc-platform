package com.jiangtj.platform.auth.context;

import lombok.Setter;

public class DefaultAuthContext implements AuthContext {

    private final Subject subject;
    @Setter
    private Authorization authorization;

    public DefaultAuthContext(Subject subject) {
        this(subject, Authorization.empty());
    }

    public DefaultAuthContext(Subject subject, Authorization authorization) {
        this.subject = subject;
        this.authorization = authorization;
    }

    @Override
    public Subject subject() {
        return subject;
    }

    @Override
    public Authorization authorization() {
        return authorization;
    }
}
