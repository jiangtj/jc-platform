package com.jiangtj.platform.spring.cloud.system;

import org.springframework.lang.Nullable;

import java.util.List;

public interface RoleService {

    void sync(@Nullable List<RoleSyncDto> list);

    // void registerRole();

}
