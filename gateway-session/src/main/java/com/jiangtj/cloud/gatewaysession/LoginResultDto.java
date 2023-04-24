package com.jiangtj.cloud.gatewaysession;

import com.jiangtj.cloud.auth.UserClaims;
import lombok.Data;

@Data
public class LoginResultDto {
    private SystemUser user;
    private UserClaims claims;
}
