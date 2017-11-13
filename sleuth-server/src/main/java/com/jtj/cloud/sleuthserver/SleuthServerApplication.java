package com.jtj.cloud.sleuthserver;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.sleuth.zipkin.stream.EnableZipkinStreamServer;

@EnableZipkinStreamServer
@SpringCloudApplication
public class SleuthServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SleuthServerApplication.class, args);
	}
}
