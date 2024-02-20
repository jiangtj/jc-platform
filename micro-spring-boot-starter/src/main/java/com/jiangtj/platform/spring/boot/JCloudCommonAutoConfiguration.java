package com.jiangtj.platform.spring.boot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiangtj.platform.common.JsonUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class JCloudCommonAutoConfiguration {

    @Resource
    private ObjectMapper mapper;

    @PostConstruct
    public void setup() {
        JsonUtils.init(mapper);
    }

    @Bean
    public ApplicationProperty applicationProperty() {
        return new ApplicationProperty();
    }

}
