package com.jiangtj.platform.auth.context;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public interface RbacAuthorization extends Authorization {

    RoleProvider roleProvider();

    default List<String> specialPermissions(){
        return List.of();
    }

    default List<String> permissions() {
        return RoleProvider.getPermissionKeys(this);
    }

    static DefaultRbacAuthorization create(RoleProvider roleProvider, String... roles) {
        return create(roleProvider, Arrays.asList(roles), Collections.emptyList());
    }

    static DefaultRbacAuthorization create(RoleProvider roleProvider, List<String> roles) {
        return create(roleProvider, roles, Collections.emptyList());
    }

    static DefaultRbacAuthorization create(RoleProvider roleProvider, List<String> roles, String... permissions) {
        return create(roleProvider, roles, Arrays.asList(permissions));
    }

    static DefaultRbacAuthorization create(RoleProvider roleProvider, List<String> roles, List<String> permissions) {
        return new DefaultRbacAuthorization(roleProvider, roles, permissions);
    }
}
