package com.jiangtj.platform.system;

import com.jiangtj.platform.test.JCloudWebTest;
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
