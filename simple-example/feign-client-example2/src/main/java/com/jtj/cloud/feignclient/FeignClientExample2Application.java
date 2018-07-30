package com.jtj.cloud.feignclient;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringCloudApplication
public class FeignClientExample2Application {

	public static void main(String[] args) {
		SpringApplication.run(FeignClientExample2Application.class, args);
	}
}
