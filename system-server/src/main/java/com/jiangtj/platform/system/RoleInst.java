package com.jiangtj.platform.system;

import com.jiangtj.platform.spring.cloud.system.Permission;
import com.jiangtj.platform.spring.cloud.system.Role;

public interface RoleInst {
    Role SYSTEM = Role.of("system", "系统管理员",
        Permission.of("system:user:write"),
        Permission.of("system:user:read"),
        Permission.of("system:role"));

    Role SYSTEM_READONLY = Role.of("system-readonly", "只读系统管理员",
        Permission.of("system:user:read"));
}
