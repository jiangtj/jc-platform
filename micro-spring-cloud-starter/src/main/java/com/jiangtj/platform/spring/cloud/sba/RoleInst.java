package com.jiangtj.platform.spring.cloud.sba;

import com.jiangtj.platform.spring.cloud.system.Role;

public interface RoleInst {
    Role ACTUATOR = Role.of("actuator", "监控");
}
