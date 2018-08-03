package com.jtj.cloud.configclient;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.Duration;

/**
 * Created by MrTT (jiang.taojie@foxmail.com)
 * 2018/3/9.
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties("jwt")
public class JwtConfig implements Serializable {

    private String header = "Authorization";
    private String prefix = "Bearer ";
    private String secret = "secret";
    private Duration timeout = Duration.ofHours(10L);

}
