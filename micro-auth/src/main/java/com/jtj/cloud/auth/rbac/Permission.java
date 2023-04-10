package com.jtj.cloud.auth.rbac;

import java.util.List;

public record Permission(String name, String description) {

    @Override
    public String toString() {
        return name;
    }

    public static Permission of(String name) {
        return of(name, "");
    }

    public static Permission of(String name, String description) {
        return new Permission(name, description);
    }

    public static List<Permission> of(List<String> p) {
        return p.stream()
            .map(Permission::of)
            .toList();
    }

}
