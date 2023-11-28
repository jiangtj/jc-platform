package com.jiangtj.platform.sql;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Slf4j
@EnableConfigurationProperties(SQLPublicProperties.class)
@AutoConfigureOrder
public class SQLAutoConfiguration {

}
