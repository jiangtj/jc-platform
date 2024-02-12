package com.jiangtj.platform.sql.jooq;

import lombok.extern.slf4j.Slf4j;
import org.jooq.meta.jaxb.Configuration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

@Slf4j
@ConditionalOnClass(Configuration.class)
public class MircoJOOQMetaAutoConfiguration {

    @Bean
    public GenerateService generateService() {
        return new GenerateService();
    }

}
