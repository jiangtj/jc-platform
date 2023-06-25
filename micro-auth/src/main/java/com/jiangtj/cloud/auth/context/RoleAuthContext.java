package com.jiangtj.cloud.auth.context;

import com.jiangtj.cloud.auth.rbac.RoleProvider;

import java.util.List;

public interface RoleAuthContext extends AuthContext {

    List<String> roles();

    RoleProvider roleProvider();

    default List<String> permissions() {
        return roleProvider().getPermissionKeys(roles().toArray(String[]::new));
    }

}
