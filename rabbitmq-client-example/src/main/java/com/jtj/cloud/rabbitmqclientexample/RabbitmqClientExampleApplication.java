package com.jtj.cloud.rabbitmqclientexample;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RabbitmqClientExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(RabbitmqClientExampleApplication.class, args);
	}
}
