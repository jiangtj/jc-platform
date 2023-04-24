package com.jiangtj.cloud.system.dto;

import com.jiangtj.cloud.auth.UserClaims;
import com.jiangtj.cloud.system.entity.SystemUser;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class LoginResultDto {
    private SystemUser user;
    private UserClaims claims;
}
