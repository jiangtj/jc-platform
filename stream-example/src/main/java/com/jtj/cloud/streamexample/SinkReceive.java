package com.jtj.cloud.streamexample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;

/**
 * Created by MrTT (jiang.taojie@foxmail.com)
 * 2017/11/13.
 */
@EnableBinding(Processor.class)
public class SinkReceive {

    private static Logger logger = LoggerFactory.getLogger(SinkReceive.class);

    @StreamListener(Processor.INPUT)
    public void receive(Object payload) {
        logger.error("Received: " + payload);
    }
}
