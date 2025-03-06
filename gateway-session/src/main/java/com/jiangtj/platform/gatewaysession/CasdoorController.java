package com.jiangtj.platform.gatewaysession;

import jakarta.annotation.Resource;
import org.casbin.casdoor.entity.User;
import org.casbin.casdoor.exception.AuthException;
import org.casbin.casdoor.service.AuthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
public class CasdoorController {

    @Resource
    private AuthService casdoorAuthService;

    @RequestMapping("login")
    public Mono<String> login() {
        return Mono.just("redirect:" + casdoorAuthService.getSigninUrl("http://localhost:9999/callback"));
    }

    @GetMapping("user")
    public Mono<User> user(ServerWebExchange exchange) {
        return exchange.getSession().mapNotNull(session ->
            session.getAttribute("casdoorUser"));
    }

    @RequestMapping("callback")
    public Mono<String> callback(String code, String state, ServerWebExchange exchange) {
        String token = "";
        User user = null;
        try {
            token = casdoorAuthService.getOAuthToken(code, state);
            user = casdoorAuthService.parseJwtToken(token);
        } catch(AuthException e) {
            e.printStackTrace();
        }
        User finalUser = user;
        return exchange.getSession().flatMap(session -> {
            session.getAttributes().put("casdoorUser", finalUser);
            return Mono.just("redirect:/");
        });
    }
}
