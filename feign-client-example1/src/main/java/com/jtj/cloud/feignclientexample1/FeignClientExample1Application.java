package com.jtj.cloud.feignclientexample1;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringCloudApplication
public class FeignClientExample1Application {

	public static void main(String[] args) {
		SpringApplication.run(FeignClientExample1Application.class, args);
	}
}
