package com.jiangtj.cloud.system.dto;

import com.jiangtj.cloud.auth.system.Role;

import java.util.List;

public record RoleResultDto (String name, List<Role> defs) {
}
