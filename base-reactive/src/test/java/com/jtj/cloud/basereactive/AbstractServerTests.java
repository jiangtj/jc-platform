package com.jtj.cloud.basereactive;

import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Created At 2020/10/19.
 */
// @ActiveProfiles("testcase")
@SpringBootTest
@AutoConfigureWebTestClient
public abstract class AbstractServerTests {

}
