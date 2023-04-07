package com.jtj.cloud.sql;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class SQLPropertiesConfigurer implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        ConfigurableEnvironment environment = event.getEnvironment();
        String url = environment.getProperty("db.url");
        if (!StringUtils.hasText(url)) return;

        String username = environment.getProperty("db.username");
        String password = environment.getProperty("db.password");
        MutablePropertySources propertySources = environment.getPropertySources();
        Map<String, Object> myMap = new HashMap<>();
        myMap.put("spring.datasource.url", "jdbc:" + url);
        myMap.put("spring.datasource.username", username);
        myMap.put("spring.datasource.password", password);
        myMap.put("spring.liquibase.url", "jdbc:" + url);
        myMap.put("spring.liquibase.user", username);
        myMap.put("spring.liquibase.password", password);
        myMap.put("spring.r2dbc.url", "r2dbc:" + url);
        myMap.put("spring.r2dbc.username", username);
        myMap.put("spring.r2dbc.password", password);
        propertySources.addLast(new MapPropertySource("J_CLOUD_PLATFORM_SQL_MAP", myMap));
    }
}
