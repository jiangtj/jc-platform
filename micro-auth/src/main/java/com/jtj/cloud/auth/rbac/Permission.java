package com.jtj.cloud.auth.rbac;

import java.util.List;

public interface Permission {

    String name();

    String description();

    static Permission of(String name) {
        return of(name, "");
    }

    static Permission of(String name, String description) {
        return new PermissionRecord(name, description);
    }

    static List<Permission> of(List<String> p) {
        return p.stream()
            .map(Permission::of)
            .toList();
    }

    record PermissionRecord(String name, String description) implements Permission {}

}
