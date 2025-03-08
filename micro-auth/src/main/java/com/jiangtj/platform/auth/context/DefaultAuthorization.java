package com.jiangtj.platform.auth.context;

import java.util.List;

public class DefaultAuthorization implements Authorization{

    public final List<String> roles;
    public final List<String> permissions;

    public static final Authorization EMPTY = new DefaultAuthorization();

    public DefaultAuthorization() {
        this(List.of());
    }

    public DefaultAuthorization(List<String> roles) {
        this(roles, List.of());
    }

    public DefaultAuthorization(List<String> roles, List<String> permissions) {
        this.roles = roles;
        this.permissions = permissions;
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
