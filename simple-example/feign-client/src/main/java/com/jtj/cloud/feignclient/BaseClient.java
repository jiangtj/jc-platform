package com.jtj.cloud.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by jiang (jiang.taojie@foxmail.com)
 * 2017/8/2 22:56 End.
 */
@FeignClient(value = "base-client", fallback = BaseClientImpl.class)
public interface BaseClient {

    @GetMapping("/")
    String getBaseClientData();

}
