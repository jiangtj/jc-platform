package com.jtj.cloud.system;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class BaseController {

    @GetMapping("/")
    public Mono<String> index(){
        return Mono.just("System Client Started !!");
    }

}
