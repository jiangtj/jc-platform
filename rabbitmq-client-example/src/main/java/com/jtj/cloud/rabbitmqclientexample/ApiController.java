package com.jtj.cloud.rabbitmqclientexample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jiang (jiang.taojie@foxmail.com)
 * 2017/10/19 23:20 End.
 */
@RestController
public class ApiController {

    @Autowired
    Sender sender;

    @GetMapping("/send/{msg}")
    public String send(@PathVariable("msg") String msg) {
        sender.send(msg);
        return "ok!";
    }

}
