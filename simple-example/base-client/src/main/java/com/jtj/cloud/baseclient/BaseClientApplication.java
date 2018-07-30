package com.jtj.cloud.baseclient;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringCloudApplication
public class BaseClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(BaseClientApplication.class, args);
	}

	@GetMapping("/")
	public String index(){
		return "Hello !!";
	}

}
