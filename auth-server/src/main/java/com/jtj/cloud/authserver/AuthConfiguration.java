package com.jtj.cloud.authserver;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Created At 2021/3/24.
 */
@AutoConfiguration
@EnableConfigurationProperties(AuthProperties.class)
public class AuthConfiguration {

    @Bean
    public AuthServer authServer() {
        return new AuthServer();
    }

}
