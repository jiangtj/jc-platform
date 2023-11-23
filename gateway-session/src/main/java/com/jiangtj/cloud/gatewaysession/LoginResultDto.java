package com.jiangtj.cloud.gatewaysession;

import lombok.Data;

import java.util.List;

@Data
public class LoginResultDto {
    private SystemUser user;
    private List<String> roles;
}
