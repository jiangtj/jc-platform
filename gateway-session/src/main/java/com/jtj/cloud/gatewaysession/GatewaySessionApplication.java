package com.jtj.cloud.gatewaysession;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewaySessionApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewaySessionApplication.class, args);
	}

}
