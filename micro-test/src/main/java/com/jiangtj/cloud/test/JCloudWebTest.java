package com.jiangtj.cloud.test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ExtendWith(JCloudExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
@ActiveProfiles("testcase")
public @interface JCloudWebTest {
}
