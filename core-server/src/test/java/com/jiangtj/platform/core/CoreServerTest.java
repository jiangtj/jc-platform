package com.jiangtj.platform.core;

import com.jiangtj.platform.test.cloud.JMicroCloudMvcTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
@JMicroCloudMvcTest
class CoreServerTest {

    @Test
    void load() {
        log.info("Application Test Env is normal.");
    }

}