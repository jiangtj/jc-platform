package com.jiangtj.platform.spring.cloud.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleSyncDto {
    private String key;
    private String name;
}
