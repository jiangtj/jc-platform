package com.jiangtj.cloud.auth.rbac;

import com.jiangtj.cloud.auth.AuthUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DefaultRoleProvider implements RoleProvider{

    private final List<String> roles;
    private final Map<String, Role> keyToRole;

    private List<String> keys;

    public DefaultRoleProvider(ObjectProvider<List<Role>> op) {
        List<Role> roleList = op.getIfAvailable(Collections::emptyList);
        roles = roleList.stream().map(Role::name).toList();
        keyToRole = roleList.stream().collect(Collectors.toMap(
            x -> AuthUtils.toKey(x.name()),
            Function.identity()));
    }

    @Override
    public List<String> getRoles() {
        return roles;
    }

    @Override
    public List<String> getRoleKeys() {
        if (CollectionUtils.isEmpty(keys)) {
            keys = RoleProvider.super.getRoleKeys();
        }
        return keys;
    }

    @Override
    public Role getRole(String key) {
        return keyToRole.get(key);
    }

    @Override
    public List<Permission> getPermissions(String key) {
        return Optional.ofNullable(getRole(key))
            .map(Role::permissions)
            .orElse(Collections.emptyList());
    }
}
