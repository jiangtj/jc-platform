package com.jiangtj.platform.baseservlet;

import com.jiangtj.platform.auth.annotations.HasLogin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/anno")
public class RBACController {

    @HasLogin
    @GetMapping("/hasLogin")
    public String hasLogin(){
        return "hasLogin !!";
    }

}
