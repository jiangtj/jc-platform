package com.jiangtj.platform.spring.cloud.system;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

public class DefaultRoleServiceImpl implements RoleService {

    @Resource
    private SystemInstanceApi api;
    @Resource
    private TaskScheduler taskScheduler;
    @Resource
    private ObjectProvider<List<Role>> roleProvider;

    @PostConstruct
    public void init() {
        taskScheduler.schedule(() -> {
            List<Role> roleList = roleProvider.getIfAvailable(Collections::emptyList);
            if (roleList.isEmpty()) {
                return;
            }
            List<RoleSyncDto> syncList = roleList.stream()
                .map(r -> new RoleSyncDto(r.name(), r.description()))
                .toList();
            sync(syncList);
        }, Instant.now().plusSeconds(15));
    }

    @Override
    public void sync(@Nullable List<RoleSyncDto> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        api.syncRoles(list);
    }
}
