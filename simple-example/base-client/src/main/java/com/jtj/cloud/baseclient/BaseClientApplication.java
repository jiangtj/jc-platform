package com.jtj.cloud.baseclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@EnableDiscoveryClient
public class BaseClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(BaseClientApplication.class, args);
	}

	@GetMapping("/")
	public String index(){
		return "Base Client Started !!";
	}

}
