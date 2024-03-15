package com.jiangtj.platform.spring.cloud.core;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import reactor.core.publisher.Mono;

@HttpExchange(contentType = MediaType.APPLICATION_JSON_VALUE)
public interface ReactiveCoreInstanceApi {

    @GetExchange("/service/{kid}/publickey")
    Mono<String> getToken(@PathVariable String kid);

}
