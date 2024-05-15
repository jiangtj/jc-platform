package com.jiangtj.platform.spring.cloud.system;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "j.system")
public class RoleProperties {
    private List<RoleSyncDto> roles;
}
