package com.jiangtj.cloud.system.dto;

import com.jiangtj.cloud.system.entity.SystemUser;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor(staticName = "of")
public class LoginResultDto {
    private SystemUser user;
    private List<String> roles;
}
