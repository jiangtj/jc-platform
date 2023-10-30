package com.jiangtj.cloud.auth;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Data
@ConfigurationProperties(prefix = "auth")
public class AuthProperties {
    private String secret;
    private Duration expires = Duration.ofMinutes(5);
    private Duration maxExpires = Duration.ofMinutes(10);
    private boolean notifyCoreServer = true;
    private int notifyCoreServerDelay = 15;
    private boolean initLoadBalancedClient = true;
}
