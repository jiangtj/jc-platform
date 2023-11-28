package com.jiangtj.platform.gatewaysession;

import lombok.Data;

import java.util.List;

@Data
public class LoginResultDto {
    private SystemUser user;
    private List<String> roles;
}
