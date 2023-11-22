package com.jiangtj.cloud.system;

import com.jiangtj.cloud.auth.system.Permission;
import com.jiangtj.cloud.auth.system.Role;

public interface RoleInst {
    Role SYSTEM = Role.of("system", "系统管理员",
        Permission.of("system:user:write"),
        Permission.of("system:user:read"),
        Permission.of("system:role"));

    Role SYSTEM_READONLY = Role.of("system-readonly", "只读系统管理员",
        Permission.of("system:user:read"));
}
