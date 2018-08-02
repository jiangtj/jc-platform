package com.jtj.cloud.configclient;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

@RestController
@SpringCloudApplication
public class ConfigClientApplication {

	@Resource
	private JwtConfig config;

	public static void main(String[] args) {
		SpringApplication.run(ConfigClientApplication.class, args);
	}

	@GetMapping("/")
	public String index(){
		return "index";
	}

	@GetMapping("/config")
	public Mono<JwtConfig> config(){
		return Mono.just(config);
	}

}
