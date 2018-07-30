package com.jtj.cloud.feignclient.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by jiang (jiang.taojie@foxmail.com)
 * 2017/8/2 22:56 End.
 */
@FeignClient(value = "feign-client-example1",fallback = Example1ServiceImpl.class)
public interface Example1Service {

    @GetMapping("show")
    String show();

}
