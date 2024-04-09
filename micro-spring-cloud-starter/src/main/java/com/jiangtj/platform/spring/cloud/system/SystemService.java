package com.jiangtj.platform.spring.cloud.system;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.scheduling.TaskScheduler;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

public class SystemService {

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
        }, Instant.now().plusSeconds(60));
    }


}
