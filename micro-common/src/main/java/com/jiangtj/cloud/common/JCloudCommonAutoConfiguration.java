package com.jiangtj.cloud.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiangtj.cloud.common.utils.JsonUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.AutoConfiguration;

@AutoConfiguration
public class JCloudCommonAutoConfiguration {

    @Resource
    private ObjectMapper mapper;

    @PostConstruct
    public void setup() {
        JsonUtils.init(mapper);
    }

}
