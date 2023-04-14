package com.jtj.cloud.gatewaysession;

import com.jtj.cloud.auth.UserClaims;
import lombok.Data;

@Data
public class LoginResultDto {
    private SystemUser user;
    private UserClaims claims;
}
