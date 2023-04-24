package com.jiangtj.cloud.system.entity;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table
public class SystemUserRole {
    private Long userId;
    private String role;
}
