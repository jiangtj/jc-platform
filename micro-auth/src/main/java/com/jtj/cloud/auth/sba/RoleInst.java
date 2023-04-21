package com.jtj.cloud.auth.sba;

import com.jtj.cloud.auth.rbac.Role;

public interface RoleInst {
    Role ACTUATOR = Role.of("actuator", "监控");
}
