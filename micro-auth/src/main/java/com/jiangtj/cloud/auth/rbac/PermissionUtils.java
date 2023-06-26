package com.jiangtj.cloud.auth.rbac;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public interface PermissionUtils {

    /**
     * Get a list of role's permission keys
     */
    static List<String> getPermissionKeys(RoleProvider provider, String... roles) {
        return Arrays.stream(roles)
            .map(provider::getPermissions)
            .flatMap(Collection::stream)
            .map(Permission::name)
            .toList();
    }

}
