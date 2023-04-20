package com.jtj.cloud.auth.rbac;

import com.jtj.cloud.auth.AuthUtils;

import java.util.Collections;
import java.util.List;

public final class Role {
    private final String name;
    private final String key;
    private final String description;
    private final List<Permission> permissions;

    public Role(String name, String description, List<Permission> permissions) {
        this.name = name;
        this.key = AuthUtils.toKey(name);
        this.description = description;
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        return name;
    }

    public String key() {
        return key;
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public List<Permission> permissions() {
        return permissions;
    }

    public static Role of(String name) {
        return of(name, "");
    }

    public static Role of(String name, String description) {
        return of(name, description, Collections.emptyList());
    }

    public static Role of(String name, String description, List<Permission> permissions) {
        return new Role(name, description, permissions);
    }
}
