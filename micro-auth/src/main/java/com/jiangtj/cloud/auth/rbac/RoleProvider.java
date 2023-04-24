package com.jiangtj.cloud.auth.rbac;

import com.jiangtj.cloud.auth.AuthUtils;
import org.springframework.lang.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public interface RoleProvider {
    /**
     * Def roles such as system system-read etc.
     */
    List<String> getRoles();

    /**
     * Trim role name, such as system-R_ead to systemread
     * We suggest over this, and cache result
     */
    default List<String> getRoleKeys() {
        return getRoles().stream().map(AuthUtils::toKey).toList();
    }

    /**
     * Def roles such as system system-read etc.
     */
    @Nullable
    Role getRole(String key);

    /**
     * Get role's permission
     * if role is null, return emptyList
     */
    List<Permission> getPermissions(String key);

    /**
     * Get a list of role's permission keys
     */
    default List<String> getPermissionKeys(String... keys) {
        return Arrays.stream(keys)
            .map(this::getPermissions)
            .flatMap(Collection::stream)
            .map(Permission::name)
            .toList();
    }
}
