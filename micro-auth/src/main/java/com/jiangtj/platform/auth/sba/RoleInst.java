package com.jiangtj.platform.auth.sba;

import com.jiangtj.platform.auth.system.Role;

public interface RoleInst {
    Role ACTUATOR = Role.of("actuator", "监控");
}
