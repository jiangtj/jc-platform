package com.jtj.cloud.system;

import com.jtj.cloud.test.JCloudWebTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
@JCloudWebTest
class ApplicationLoadTests {

    @Test
    void load() {
        log.info("Application Test Env is normal.");
    }
    
}
