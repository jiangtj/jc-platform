package com.jtj.cloud.feignclientexample1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@EnableFeignClients
@SpringCloudApplication
public class FeignClientExample1Application {

	public static void main(String[] args) {
		SpringApplication.run(FeignClientExample1Application.class, args);
	}
}
