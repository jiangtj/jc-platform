package com.jtj.cloud.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.Nullable;

public class SpringApplicationContextHolder implements ApplicationContextAware {

    private static ApplicationContext application;

    @Nullable
    public static ApplicationContext getApplicationContext() {
        return application;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        application = applicationContext;
    }

}
