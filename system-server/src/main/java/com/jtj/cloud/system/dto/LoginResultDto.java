package com.jtj.cloud.system.dto;

import com.jtj.cloud.auth.UserClaims;
import com.jtj.cloud.system.entity.SystemUser;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class LoginResultDto {
    private SystemUser user;
    private UserClaims claims;
}
