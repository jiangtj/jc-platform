package com.jtj.cloud.basereactive;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * Created by MrTT (jiang.taojie@foxmail.com)
 * 2018/7/31.
 */
@Slf4j
@RestController
public class CurlController {

    private static ObjectMapper objectMapper;

    @GetMapping("/curl/{id}")
    public Mono<String> get(@PathVariable Long id){
        return Mono.just(id).map(item -> "Your id is " + item);
    }

    @GetMapping("/curl")
    public Mono<String> get(@RequestParam MultiValueMap<String,String> queryParams){
        return Mono.just(queryParams).map(item -> "Your QueryParams is " + item);
    }

    @PostMapping("/curl")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<String> post(@RequestBody Mono<User> user){
        return user.map(CurlController::toJson).map(item -> "Your Body is " + item);
    }

    @PutMapping("/curl")
    public Mono<String> put(@RequestBody Mono<User> user){
        return post(user);
    }

    @DeleteMapping("/curl")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete() {
        throw new RuntimeException("Fail");
    }

    private static ObjectMapper getObjectMapper(){
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        return objectMapper;
    }
    private static String toJson(Object object) {
        ObjectMapper mapper = getObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
