package com.jtj.cloud.rabbitmqclientexample;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by jiang (jiang.taojie@foxmail.com)
 * 2017/10/19 23:38 End.
 */
@Configuration
public class RabbitConfig {

    @Bean
    public Queue helloQueue() {
        return new Queue("hello");
    }

}
