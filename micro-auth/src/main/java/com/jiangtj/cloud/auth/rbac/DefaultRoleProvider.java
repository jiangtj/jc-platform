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

    private final ObjectProvider<List<Role>> op;

    private List<String> roles;
    private Map<String, Role> keyToRole;
    private List<String> keys;

    /**
     * @param op for lazy get role
     */
    public DefaultRoleProvider(ObjectProvider<List<Role>> op) {
        this.op = op;
    }

    @Override
    public List<String> getRoles() {
        if (CollectionUtils.isEmpty(roles)) {
            List<Role> roleList = op.getIfAvailable(Collections::emptyList);
            roles = roleList.stream().map(Role::name).toList();
        }
        return roles;
    }

    @Override
    public Role getRole(String key) {
        if (CollectionUtils.isEmpty(keyToRole)) {
            List<Role> roleList = op.getIfAvailable(Collections::emptyList);
            keyToRole = roleList.stream().collect(Collectors.toMap(
                x -> AuthUtils.toKey(x.name()),
                Function.identity()));
        }
        return keyToRole.get(key);
    }

    @Override
    public List<Permission> getPermissions(String key) {
        return Optional.ofNullable(getRole(key))
            .map(Role::permissions)
            .orElse(Collections.emptyList());
    }
}
