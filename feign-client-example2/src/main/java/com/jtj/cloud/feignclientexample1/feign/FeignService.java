package com.jtj.cloud.feignclientexample1.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by jiang (jiang.taojie@foxmail.com)
 * 2017/8/2 22:56 End.
 */
@FeignClient("feign-client-example1")
public interface FeignService {

    @GetMapping("show")
    String show();

}
