package com.jtj.cloud.basereactive.base;

import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;

// @ActiveProfiles("testcase")
@SpringBootTest
@AutoConfigureWebTestClient
public abstract class AbstractServerTests {

}
