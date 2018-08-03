package com.jtj.cloud.configclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

@Slf4j
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
		JwtConfig copyConfig = new JwtConfig();
		BeanUtils.copyProperties(config,copyConfig);
		return Mono.just(copyConfig);
	}

}
