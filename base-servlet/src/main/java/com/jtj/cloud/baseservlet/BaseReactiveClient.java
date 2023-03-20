package com.jtj.cloud.baseservlet;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("base-reactive")
public interface BaseReactiveClient {
    @GetMapping( "/")
    String getIndex();

    @GetMapping("/needtoken")
    String getNeedToken();
}
