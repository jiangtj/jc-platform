package com.jiangtj.platform.spring.cloud.core;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PutExchange;

@HttpExchange(contentType = MediaType.APPLICATION_JSON_VALUE)
public interface CoreInstanceApi {

    @GetExchange("/publickey/admin")
    String getAdminToken();

    @PutExchange("/publickey/{kid}")
    void registerToken(@PathVariable String kid, @RequestBody RegisterPublicKey register);

    @GetExchange("/publickey/{kid}")
    String getToken(@PathVariable String kid);

}
