package com.jiangtj.platform.auth.context;

import java.util.List;

public class DefaultRoleProviderContext implements RoleProviderAuthContext {

    private final String subject;
    private final RoleProvider provider;
    private final List<String> roles;
    private final List<String> permissions;

    public DefaultRoleProviderContext(String subject, RoleProvider provider, List<String> roles, List<String> permissions) {
        this.subject = subject;
        this.provider = provider;
        this.roles = roles;
        this.permissions = permissions;
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
    public RoleProvider roleProvider() {
        return provider;
    }

    @Override
    public List<String> specialPermissions() {
        return permissions;
    }
}
