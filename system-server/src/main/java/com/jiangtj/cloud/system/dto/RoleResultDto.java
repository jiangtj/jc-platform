package com.jiangtj.cloud.system.dto;

import com.jiangtj.cloud.auth.rbac.Role;

import java.util.List;

public record RoleResultDto (String name, List<Role> defs) {
}
