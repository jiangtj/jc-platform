package com.jiangtj.cloud.core.pk;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("pk.task")
public class PKTaskProperties {
    private int delay = 600;
    private int initialDelay = 10;
    private int upDelay = 3600;
}
