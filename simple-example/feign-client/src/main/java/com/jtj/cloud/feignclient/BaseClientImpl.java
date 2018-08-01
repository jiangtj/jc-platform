package com.jtj.cloud.feignclient;

import org.springframework.stereotype.Component;

import java.util.Map;

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

    @Override
    public String getUser(Long id) {
        return null;
    }

    @Override
    public String getUser(Map<String, String> query) {
        return null;
    }

    @Override
    public String postUser(User user) {
        return null;
    }

    @Override
    public String putUser(User user) {
        return null;
    }

    @Override
    public void deleteUser() {
        throw new RuntimeException("Delete Fail !!");
    }

}
