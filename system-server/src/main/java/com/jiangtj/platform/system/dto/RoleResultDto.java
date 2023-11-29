package com.jiangtj.platform.system.dto;

import com.jiangtj.platform.auth.cloud.system.Role;

import java.util.List;

public record RoleResultDto (String name, List<Role> defs) {
}
