package com.jiangtj.cloud.auth.rbac;

import org.springframework.lang.Nullable;

import java.util.List;

public interface RoleProvider {
    /**
     * Def roles such as system system-read etc.
     * Note: role key will remove `-` ` ` `_`e, such as system-R_ead key is systemread
     */
    List<String> getRoles();

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

}
