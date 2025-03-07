package com.jiangtj.platform.baseservlet;

import com.jiangtj.platform.auth.casdoor.CasdoorUserContextImpl;
import com.jiangtj.platform.auth.servlet.AuthHolder;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.casbin.casdoor.entity.User;
import org.casbin.casdoor.service.AuthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class CasdoorController {

    @Resource
    private AuthService casdoorAuthService;

    @GetMapping("toLogin")
    public String toLogin(String redirectUrl) {
        return casdoorAuthService.getSigninUrl(redirectUrl);
    }

    @GetMapping("user")
    public User user() {
        return ((CasdoorUserContextImpl)AuthHolder.getAuthContext()).getCasdoorUser();
    }

    @PostMapping("login")
    public String login(@RequestParam String code,@RequestParam String state) {
        return casdoorAuthService.getOAuthToken(code, state);
    }
}
