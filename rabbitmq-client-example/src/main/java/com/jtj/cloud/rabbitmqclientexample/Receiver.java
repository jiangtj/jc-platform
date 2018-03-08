package com.jtj.cloud.rabbitmqclientexample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Created by jiang (jiang.taojie@foxmail.com)
 * 2017/10/19 23:08 End.
 */
@Component
@RabbitListener(queues = "hello")
public class Receiver {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RabbitHandler
    public void handler(String msg) {
        logger.error(msg);
    }

}
