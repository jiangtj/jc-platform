package com.jiangtj.platform.system.dto;

import com.jiangtj.platform.system.jooq.tables.pojos.SystemRole;
import com.jiangtj.platform.system.jooq.tables.pojos.SystemRoleCreator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class RoleDto {
    private SystemRole info;
    private List<SystemRoleCreator> creators;
}
