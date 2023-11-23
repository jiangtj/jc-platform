package com.jiangtj.cloud.auth.sba;

import com.jiangtj.cloud.auth.system.Role;

public interface RoleInst {
    Role ACTUATOR = Role.of("actuator", "监控");
}
