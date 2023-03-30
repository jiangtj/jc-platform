package com.jtj.cloud.system.dto;

import lombok.Data;

/**
 * 2021/1/7 22:23 End.
 */
@Data
public class PasswordUpdateDto {
    private Long adminId;
    private String old;
    private String password;
}
