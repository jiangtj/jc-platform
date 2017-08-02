package com.jtj.cloud.feignclientexample1.feign;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * Created by jiang (jiang.taojie@foxmail.com)
 * 2017/8/2 22:56 End.
 */
@Component
public class FeignServiceImpl implements FeignService {

    @Override
    public String show() {
        return "error: not fetch data!";
    }

    /*@Override
    public FeignService create(Throwable throwable) {
        return () -> "error: "+throwable.getMessage();
    }*/
}
