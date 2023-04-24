package com.jiangtj.cloud.auth.sba;

import com.jiangtj.cloud.auth.rbac.Role;

public interface RoleInst {
    Role ACTUATOR = Role.of("actuator", "监控");
}
