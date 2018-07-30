package com.jtj.cloud.feignclient;

import org.springframework.stereotype.Component;

/**
 * Created by jiang (jiang.taojie@foxmail.com)
 * 2017/8/2 22:56 End.
 */
@Component
public class BaseClientImpl implements BaseClient {

    @Override
    public String getBaseClientData() {
        return "error: not fetch data!";
    }

}
