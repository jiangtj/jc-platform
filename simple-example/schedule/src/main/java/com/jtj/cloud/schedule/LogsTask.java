package com.jtj.cloud.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * Created by MrTT (jiang.taojie@foxmail.com)
 * 2018/9/19.
 */
@Slf4j
@Component
public class LogsTask {

    @Scheduled(cron = "*/5 * * * * ?")
    public void logs(){
        log.error(Instant.now().toString());
    }

}
