package com.jtj.cloud.sbaserver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created At 2021/3/24.
 */
@Configuration
public class AuthConfiguration {

    @Bean
    public AuthServer authServer() {
        return new AuthServer();
    }

    @Bean
    public AuthProperties authProperties() {
        return new AuthProperties();
    }

}
