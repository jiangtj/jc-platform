package com.jiangtj.cloud.auth.context;

import com.jiangtj.cloud.auth.UserClaims;
import com.jiangtj.cloud.auth.rbac.RoleProvider;

import java.util.List;

public interface RoleAuthContext extends AuthContext {

    UserClaims user();

    RoleProvider roleProvider();

    default List<String> roles() {
        return user().roles();
    }

    default List<String> permissions() {
        return roleProvider().getPermissionKeys(roles().toArray(String[]::new));
    }

}
