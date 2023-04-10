package com.jtj.cloud.auth.rbac;

public enum RoleInst {
    PUBLIC("公共服务"),
    SUPER_ADMIN("超级管理员"),
    ADMIN("管理员"),
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
}
