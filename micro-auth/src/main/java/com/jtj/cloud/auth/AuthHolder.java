package com.jtj.cloud.auth;

import com.jtj.cloud.auth.rbac.RoleContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class AuthHolder implements ApplicationContextAware {
    private static ApplicationContext application;
    private static RoleContext roleContext;

    public static RoleContext getRoleContext() {
        if (roleContext != null) {
            return roleContext;
        }
        roleContext = application.getBean(RoleContext.class);
        return roleContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        application = applicationContext;
    }

}
