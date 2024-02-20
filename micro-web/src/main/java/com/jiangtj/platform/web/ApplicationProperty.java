package com.jiangtj.platform.web;

import jakarta.annotation.Resource;
import org.springframework.core.env.Environment;
import org.springframework.lang.Nullable;

public class ApplicationProperty {

    @Resource
    private Environment environment;

    private String applicationName;

    @Nullable
    public String getName() {
        if (applicationName != null) {
            return applicationName;
        }
        applicationName = environment.getProperty("spring.application.name");
        if (applicationName == null) {
            return null;
        }
        applicationName = applicationName.toLowerCase();
        return applicationName;
    }
}
