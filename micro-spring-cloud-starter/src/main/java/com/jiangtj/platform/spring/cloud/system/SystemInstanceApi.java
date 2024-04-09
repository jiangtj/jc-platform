package com.jiangtj.platform.spring.cloud.system;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@HttpExchange(contentType = MediaType.APPLICATION_JSON_VALUE)
public interface SystemInstanceApi {

    @GetExchange("/roles/sync")
    void syncRoles(@RequestBody List<RoleSyncDto> roles);

}
