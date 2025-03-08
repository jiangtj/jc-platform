package com.jiangtj.platform.auth.context;

import java.util.List;

/**
 * Auth 上下文
 */
public interface AuthContext {
    Subject subject();
    Authorization authorization();
    void setAuthorization(Authorization authorization);

    static AuthContext create(Subject subject) {
        return new DefaultAuthContext(subject);
    }

    static AuthContext create(Subject subject, Authorization authorization) {
        return new DefaultAuthContext(subject, authorization);
    }
}
