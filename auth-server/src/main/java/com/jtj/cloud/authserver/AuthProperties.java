package com.jtj.cloud.authserver;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.List;

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

    private boolean filter = true;
    private List<String> excludePatterns;
}