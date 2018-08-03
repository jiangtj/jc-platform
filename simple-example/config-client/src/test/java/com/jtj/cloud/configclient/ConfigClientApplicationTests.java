package com.jtj.cloud.configclient;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConfigClientApplicationTests {

	@Autowired
	private ApplicationContext context;

	private WebTestClient client;

	@Before
	public void setUp() {
		client = WebTestClient.bindToApplicationContext(context).build();
	}

	@Test
	public void contextLoads() {
		client.get().uri("/")
				.exchange()
				.expectStatus().isOk()
				.expectBody(String.class).isEqualTo("Config Client Started !!");
	}

	@Test
	public void config() {
		client.get().uri("/config")
				.exchange()
				.expectStatus().isOk()
				.expectBody(String.class).isEqualTo("{\"header\":\"Header\",\"prefix\":\"Prefix\",\"secret\":\"vfdnerjwvgkblhnxcsdceru==\",\"timeout\":\"PT8H\"}");
	}

}
