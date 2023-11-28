package com.jiangtj.platform.system.dto;

import com.jiangtj.platform.system.entity.SystemUser;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor(staticName = "of")
public class LoginResultDto {
    private SystemUser user;
    private List<String> roles;
}
