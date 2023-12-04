package com.jiangtj.platform.auth.cloud;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Data
@ConfigurationProperties(prefix = "auth")
public class AuthProperties {
    private String kid;
    private String privateKey;
    private String publicKey;
    private Duration expires = Duration.ofMinutes(5);
    private Duration maxExpires = Duration.ofMinutes(10);
    private boolean initLoadBalancedClient = true;
}
