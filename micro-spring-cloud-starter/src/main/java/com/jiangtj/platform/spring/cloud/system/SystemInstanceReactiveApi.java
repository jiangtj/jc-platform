package com.jiangtj.platform.spring.cloud.system;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@HttpExchange(contentType = MediaType.APPLICATION_JSON_VALUE)
public interface SystemInstanceReactiveApi {

    @GetExchange("/roles/sync")
    Mono<Void> syncRoles(@RequestBody List<RoleSyncDto> roles);

}
