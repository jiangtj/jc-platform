package com.jtj.cloud.auth.rbac;

import java.util.List;

@FunctionalInterface
public interface RoleContext {
    List<Role> getRoles();
}
