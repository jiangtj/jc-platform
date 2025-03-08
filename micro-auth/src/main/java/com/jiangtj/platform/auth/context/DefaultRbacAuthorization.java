package com.jiangtj.platform.auth.context;

import java.util.List;

public class DefaultRbacAuthorization implements RbacAuthorization {

    private final RoleProvider provider;
    private final List<String> roles;
    private final List<String> permissions;

    public DefaultRbacAuthorization(RoleProvider provider, List<String> roles, List<String> permissions) {
        this.provider = provider;
        this.roles = roles;
        this.permissions = permissions;
    }


    @Override
    public List<String> roles() {
        return roles;
    }

    public RoleProvider roleProvider() {
        return provider;
    }

    @Override
    public List<String> specialPermissions() {
        return permissions;
    }
}
