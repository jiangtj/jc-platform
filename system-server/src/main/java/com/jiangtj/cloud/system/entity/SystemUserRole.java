package com.jiangtj.cloud.system.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table
@AllArgsConstructor
@NoArgsConstructor
public class SystemUserRole {
    private Long userId;
    private String role;
}
