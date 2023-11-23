package com.jiangtj.cloud.auth.context;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public interface RoleProvider {

    /**
     * Get role's permission
     * if role is null, return emptyList
     */
    List<String> getPermissions(String key);

    static List<String> getPermissionKeys(RoleProvider provider, String... roles) {
        return Arrays.stream(roles)
            .map(provider::getPermissions)
            .flatMap(Collection::stream)
            .toList();
    }

}
