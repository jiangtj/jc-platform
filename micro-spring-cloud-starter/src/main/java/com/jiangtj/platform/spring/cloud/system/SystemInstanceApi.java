package com.jiangtj.platform.spring.cloud.system;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;

@HttpExchange(contentType = MediaType.APPLICATION_JSON_VALUE)
public interface SystemInstanceApi {

    @PostExchange("/roles/sync")
    void syncRoles(@RequestBody List<RoleSyncDto> roles);

}
