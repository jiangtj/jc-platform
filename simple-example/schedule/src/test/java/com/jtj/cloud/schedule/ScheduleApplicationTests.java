package com.jtj.cloud.schedule;

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
public class ScheduleApplicationTests {

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
				.expectBody(String.class).isEqualTo("Schedule Started !!");
	}

}
