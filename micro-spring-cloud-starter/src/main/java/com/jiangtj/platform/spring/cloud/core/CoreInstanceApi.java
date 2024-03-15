package com.jiangtj.platform.spring.cloud.core;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(contentType = MediaType.APPLICATION_JSON_VALUE)
public interface CoreInstanceApi {

    @GetExchange("/service/{kid}/publickey")
    String getToken(@PathVariable String kid);

}
