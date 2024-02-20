package com.jiangtj.platform.system.dto;

import com.jiangtj.platform.spring.cloud.system.Role;

import java.util.List;

public record RoleResultDto (String name, List<Role> defs) {
}
