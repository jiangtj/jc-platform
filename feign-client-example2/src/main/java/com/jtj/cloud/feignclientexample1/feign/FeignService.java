package com.jtj.cloud.feignclientexample1.feign;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by jiang (jiang.taojie@foxmail.com)
 * 2017/8/2 22:56 End.
 */
@FeignClient(value = "feign-client-example1",fallback = FeignService.class)
public interface FeignService {

    @GetMapping("show")
    String show();

}
