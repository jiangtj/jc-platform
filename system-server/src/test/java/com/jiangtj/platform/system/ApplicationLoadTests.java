package com.jiangtj.platform.system;

import com.jiangtj.platform.test.cloud.JMicroCloudFluxTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
@JMicroCloudFluxTest
class ApplicationLoadTests {

    @Test
    void load() {
        log.info("Application Test Env is normal.");
    }
    
}
