package com.jtj.cloud.sbaserver;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * 2018/9/26.
 */
@Data
@ConfigurationProperties(prefix = "auth")
public class AuthProperties {
    private String secret;
    private Duration expires = Duration.ofMinutes(5);
    private Duration maxExpires = Duration.ofMinutes(10);
    private String headerName = "Authorization";
    private String headerPrefix = "Bearer ";
}
