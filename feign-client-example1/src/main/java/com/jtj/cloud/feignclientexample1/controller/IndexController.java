package com.jtj.cloud.feignclientexample1.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by MrTT (jiang.taojie@foxmail.com)
 * 2017/8/2.
 */
@Controller
public class IndexController {

    @Value("${sometext}")
    private String sometext;

    @RequestMapping("/")
    public String index(){
        return "index";
    }

    @ResponseBody
    @GetMapping("/show")
    public String show(){
        return "Hello World! I'm Jone! This message from feign 1."+sometext;
    }

}
