package com.jtj.cloud.feignclient;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@EnableFeignClients
@SpringCloudApplication
public class FeignClientApplication {

	@Resource
	private BaseClient baseClient;

	public static void main(String[] args) {
		SpringApplication.run(FeignClientApplication.class, args);
	}

	@GetMapping("/")
	public String index(){
		return "Feign Client Started !!";
	}

	@GetMapping("/base")
	public String getBaseClientData(){
		return baseClient.getBaseClientData();
	}

}
