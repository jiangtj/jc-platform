package com.jtj.cloud.auth;

import com.jtj.cloud.auth.rbac.RoleProvider;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class AuthHolder implements ApplicationContextAware {
    private static ApplicationContext application;
    private static RoleProvider roleProvider;

    public static RoleProvider getRoleProvider() {
        if (roleProvider != null) {
            return roleProvider;
        }
        roleProvider = application.getBean(RoleProvider.class);
        return roleProvider;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        application = applicationContext;
    }

}
