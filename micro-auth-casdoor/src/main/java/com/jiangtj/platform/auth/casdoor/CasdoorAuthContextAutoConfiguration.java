package com.jiangtj.platform.auth.casdoor;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class CasdoorAuthContextAutoConfiguration {

    @Bean
    public CasdoorAuthContextConverter casdoorAuthContextConverter() {
        return new CasdoorAuthContextConverter();
    }

}
