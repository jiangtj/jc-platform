package com.jtj.cloud.system.base;

import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Created At 2020/10/19.
 */
// @ActiveProfiles("testcase")
@SpringBootTest
@AutoConfigureWebTestClient
public abstract class AbstractServerTests {

}
