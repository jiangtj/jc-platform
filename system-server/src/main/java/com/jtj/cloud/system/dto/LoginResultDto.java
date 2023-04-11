package com.jtj.cloud.system.dto;

import com.jtj.cloud.system.AdminUser;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class LoginResultDto {
    private AdminUser user;
    private String token;
}
