package com.jiangtj.cloud.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiangtj.cloud.common.utils.JsonUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.AutoConfiguration;

@AutoConfiguration
public class JCloudCommonAutoConfiguration {

    @PostConstruct
    public void setup(ObjectMapper mapper) {
        JsonUtils.init(mapper);
    }

}
