package com.jtj.cloud.basereactive;

import com.jtj.cloud.common.BaseExceptionUtils;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
public class BaseController {

    @GetMapping("/")
    public Mono<String> index(){
        return Mono.just("Base Reactive Client Started !!");
    }

    @GetMapping("/insecure/err")
    public Mono<String> err(){
        throw BaseExceptionUtils.invalidToken("insecure");
    }

    @GetMapping("/needtoken")
    public Mono<String> needToken(){
        return Mono.just("这个请求需要token");
    }
}
