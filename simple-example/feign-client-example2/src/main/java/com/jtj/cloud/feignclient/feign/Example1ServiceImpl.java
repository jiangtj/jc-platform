package com.jtj.cloud.feignclient.feign;

import org.springframework.stereotype.Component;

/**
 * Created by jiang (jiang.taojie@foxmail.com)
 * 2017/8/2 22:56 End.
 */
@Component
public class Example1ServiceImpl implements Example1Service {

    @Override
    public String show() {
        return "error: not fetch data!";
    }

}
