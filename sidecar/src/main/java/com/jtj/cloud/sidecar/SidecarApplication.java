package com.jtj.cloud.sidecar;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.sidecar.EnableSidecar;

@EnableSidecar
@SpringCloudApplication
public class SidecarApplication {

	public static void main(String[] args) {
		SpringApplication.run(SidecarApplication.class, args);
	}

}
