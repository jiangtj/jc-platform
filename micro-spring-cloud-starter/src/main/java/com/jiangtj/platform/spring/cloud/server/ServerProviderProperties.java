package com.jiangtj.platform.spring.cloud.server;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.List;

/**
 * 配置 server auth context provider properties
 */
@Data
@ConfigurationProperties(prefix = "jcloud.auth.provider.server")
public class ServerProviderProperties {

    /**
     * 是否启用 server auth context provider 过滤器，默认 启用
     * 如果启用，默认情况下，将会对 actuator 进行保护，当然你也可以更改 path 与 exclude
     */
    private boolean filterEnable = true;

    /**
     * 生效路径，默认 /actuator/**
     */
    private List<String> path = Collections.singletonList("/actuator/**");

    /**
     * 排除的路径，默认为空
     */
    private List<String> exclude = Collections.emptyList();
}
