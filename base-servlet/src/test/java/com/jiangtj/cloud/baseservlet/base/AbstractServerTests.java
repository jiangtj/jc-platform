package com.jiangtj.cloud.baseservlet.base;

import com.jiangtj.cloud.test.JCloudAuthConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

// @ActiveProfiles("testcase")
@SpringBootTest
@AutoConfigureMockMvc
@Import({JCloudAuthConfiguration.class})
public abstract class AbstractServerTests {

}
