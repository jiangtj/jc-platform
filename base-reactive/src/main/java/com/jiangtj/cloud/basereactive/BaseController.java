package com.jiangtj.cloud.basereactive;

import com.jiangtj.cloud.auth.rbac.annotations.HasRole;
import com.jiangtj.cloud.common.BaseExceptionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class BaseController {

    @GetMapping("/")
    public Mono<String> index(){
        return Mono.just("Base Reactive Client Started !!");
    }

    @GetMapping("/insecure/err")
    public Mono<String> err(){
        throw BaseExceptionUtils.badRequest("insecure");
    }

    @GetMapping("/insecure/err2")
    public String err2() {
        throw new RuntimeException("系统错误");
    }

    @GetMapping("/needtoken")
    public Mono<String> needToken(){
        return Mono.just("这个请求需要token");
    }

    @HasRole("role-test-1")
    @GetMapping("/role-test-1")
    public Mono<String> needRole1(){
        return Mono.just("这个请求需要 role-test-1");
    }

    @HasRole("role-test-2")
    @GetMapping("/role-test-2")
    public Mono<String> needRole2(){
        return Mono.just("这个请求需要 role-test-2");
    }
}
