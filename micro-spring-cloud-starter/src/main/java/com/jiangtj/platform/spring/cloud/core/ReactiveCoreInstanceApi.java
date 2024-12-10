package com.jiangtj.platform.spring.cloud.core;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PutExchange;
import reactor.core.publisher.Mono;

@HttpExchange(contentType = MediaType.APPLICATION_JSON_VALUE)
public interface ReactiveCoreInstanceApi {

    @GetExchange("/publickey/admin")
    Mono<String> getAdminToken();

    @PutExchange("/publickey/{kid}")
    void registerToken(@PathVariable String kid, @RequestBody RegisterPublicKey register);

    @GetExchange("/service/{kid}/publickey")
    Mono<String> getToken(@PathVariable String kid);

}
