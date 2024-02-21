package com.jiangtj.platform.auth.context;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Auth 上下文
 */
public interface RoleProviderAuthContext extends AuthContext {

    RoleProvider roleProvider();

    default List<String> specialPermissions() {
        return Collections.emptyList();
    }

    @Override
    default List<String> permissions() {
        return RoleProvider.getPermissionKeys(this);
    }

    @Override
    default boolean isLogin() {
        return true;
    }

    static RoleProviderAuthContext create(String subject, RoleProvider roleProvider, String... roles) {
        return create(subject, roleProvider, Arrays.asList(roles), Collections.emptyList());
    };

    static RoleProviderAuthContext create(String subject, RoleProvider roleProvider, List<String> roles) {
        return create(subject, roleProvider, roles, Collections.emptyList());
    };

    static RoleProviderAuthContext create(String subject, RoleProvider roleProvider, List<String> roles, String... permissions) {
        return create(subject, roleProvider, roles, Arrays.asList(permissions));
    };

    static RoleProviderAuthContext create(String subject, RoleProvider roleProvider, List<String> roles, List<String> permissions) {
        return new DefaultRoleProviderContext(subject, roleProvider, roles, permissions);
    };

}
