package com.jiangtj.platform.baseservlet;

import com.jiangtj.platform.common.JsonUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.casbin.casdoor.entity.User;
import org.casbin.casdoor.exception.AuthException;
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
    public User user(HttpSession session) {
        User user = (User) session.getAttribute("casdoorUser");
        return user;
    }

    @PostMapping("login")
    public String login(@RequestParam String code,@RequestParam String state) {
        String token = "";
        User user = null;
        try {
            token = casdoorAuthService.getOAuthToken(code, state);
            user = casdoorAuthService.parseJwtToken(token);
            log.error(JsonUtils.toJson(user));
        } catch (AuthException e) {
            e.printStackTrace();
        }
        return token;
    }
}
