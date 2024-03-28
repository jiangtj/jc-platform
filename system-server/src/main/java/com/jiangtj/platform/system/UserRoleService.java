package com.jiangtj.platform.system;

import com.jiangtj.platform.auth.KeyUtils;
import com.jiangtj.platform.spring.cloud.system.Role;
import com.jiangtj.platform.system.dto.RoleResultDto;
import com.jiangtj.platform.system.entity.SystemUserRole;
import com.jiangtj.platform.system.jooq.tables.records.SystemUserRoleRecord;
import jakarta.annotation.Resource;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.jiangtj.platform.system.jooq.Tables.SYSTEM_USER_ROLE;

@Service
public class UserRoleService {

    @Resource
    private DSLContext create;
    @Resource
    private RoleService roleService;

    public List<String> getUserRoles(Long id) {
        return create.selectFrom(SYSTEM_USER_ROLE)
            .where(SYSTEM_USER_ROLE.USER_ID.eq(id))
            .fetchInto(SystemUserRole.class)
            .stream()
            .map(SystemUserRole::getRole)
            .toList();
        /*return template.select(SystemUserRole.class)
            .matching(query(where("user_id").is(id)))
            .all()
            .map(SystemUserRole::getRole);*/
    }

    public List<String> updateUserRoles(Long id, List<String> roles) {
        deleteUserRoles(id);
        List<String> list = roles.stream()
            .map(KeyUtils::toKey)
            .toList();
        insertUserRoles(id, list);
        return list;
    }

    public void insertUserRoles(Long id, List<String> roles) {
        List<SystemUserRoleRecord> records = roles.stream()
            .map(str -> new SystemUserRole(id, str))
            .map(r -> create.newRecord(SYSTEM_USER_ROLE, r))
            .toList();
        create.batchInsert(records).execute();
    }

    public void deleteUserRoles(Long id) {
        create.deleteFrom(SYSTEM_USER_ROLE)
            .where(SYSTEM_USER_ROLE.USER_ID.eq(id))
            .execute();
    }

    public RoleResultDto toResult(String key) {
        List<Role> roleInfo = roleService.getRoleInfo(key);
        return new RoleResultDto(key, roleInfo);
    }
}
