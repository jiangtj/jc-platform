package com.jtj.cloud.rabbitmqclientexample;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by jiang (jiang.taojie@foxmail.com)
 * 2017/10/19 23:20 End.
 */
@Component
public class Sender {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send(String msg){
        System.err.println(msg);
        rabbitTemplate.convertAndSend("hello",msg);
    }
}
