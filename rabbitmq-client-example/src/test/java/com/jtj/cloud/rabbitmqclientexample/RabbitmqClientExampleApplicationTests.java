package com.jtj.cloud.rabbitmqclientexample;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitmqClientExampleApplicationTests {

	@Autowired
	private Sender sender;

	@Test
	public void contextLoads() {
		sender.send("ssss");
	}

}
