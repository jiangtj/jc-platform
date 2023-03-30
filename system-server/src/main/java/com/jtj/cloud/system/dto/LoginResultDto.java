package com.jtj.cloud.system.dto;

import com.jtj.cloud.system.AdminUser;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created At 2020/11/30.
 */
@Data
@AllArgsConstructor(staticName = "of")
public class LoginResultDto {
    private AdminUser user;
    private String token;
}
