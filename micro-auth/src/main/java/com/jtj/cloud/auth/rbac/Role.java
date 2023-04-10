package com.jtj.cloud.auth.rbac;

import java.util.Collections;
import java.util.List;

public record Role(String name, String description, List<Permission> permissions) {
    @Override
    public String toString() {
        return name;
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

//    public static Role of(String name) {
//
//    }
}
