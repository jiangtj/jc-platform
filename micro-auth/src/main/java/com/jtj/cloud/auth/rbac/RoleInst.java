package com.jtj.cloud.auth.rbac;

import java.util.Arrays;
import java.util.List;

public enum RoleInst {
    SYSTEM("系统管理员"),
    SYSTEM_READONLY("只读系统管理员"),
    ACTUATOR("监控");

    private final String description;

    RoleInst(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public Role role() {
        return Role.of(this.toString(), description);
    }

    public Role permission(String... perms) {
        List<Permission> list = Arrays.stream(perms)
            .map(Permission::of)
            .toList();
        return Role.of(this.toString(), description, list);
    }

    public Role permission(Permission... perms) {
        return Role.of(this.toString(), description, Arrays.asList(perms));
    }
}
