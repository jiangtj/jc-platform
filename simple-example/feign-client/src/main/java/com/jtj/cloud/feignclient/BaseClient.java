package com.jtj.cloud.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by jiang (jiang.taojie@foxmail.com)
 * 2017/8/2 22:56 End.
 */
@FeignClient(value = "base-client", fallback = BaseClientImpl.class)
public interface BaseClient {

    @GetMapping("/")
    String getBaseClientData();

    @GetMapping("/curl/{id}")
    String getUser(@PathVariable("id") Long id);

    @GetMapping("/curl")
    String getUser(@RequestParam Map<String,String> query);

    @PostMapping("/curl")
    String postUser(@RequestBody User user);

    @PutMapping("/curl")
    String putUser(@RequestBody User user);

    @DeleteMapping("/curl")
    void deleteUser();

}
