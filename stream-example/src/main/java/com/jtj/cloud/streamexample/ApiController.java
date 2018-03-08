package com.jtj.cloud.streamexample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by MrTT (jiang.taojie@foxmail.com)
 * 2017/11/13.
 */
@RestController
public class ApiController {

    @Autowired
    private Processor processor;

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/{data}")
    public String data(@PathVariable("data") String data){
        processor.output().send(org.springframework.messaging.support.MessageBuilder.withPayload(data).build());
        return data;
    }
}
