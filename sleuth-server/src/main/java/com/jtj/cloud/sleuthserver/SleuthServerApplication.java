package com.jtj.cloud.sleuthserver;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

@EnableRabbit
@SpringCloudApplication
public class SleuthServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SleuthServerApplication.class, args);
	}
}
