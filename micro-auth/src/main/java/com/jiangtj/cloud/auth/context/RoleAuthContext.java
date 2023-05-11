package com.jiangtj.cloud.auth.context;

import com.jiangtj.cloud.auth.AuthHolder;
import com.jiangtj.cloud.auth.UserClaims;

import java.util.List;

public interface RoleAuthContext extends AuthContext {

    UserClaims user();

    default List<String> roles() {
        return user().roles();
    }

    default List<String> permissions() {
        return AuthHolder.getRoleProvider()
            .getPermissionKeys(roles().toArray(String[]::new));
    }

}
