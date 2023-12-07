package com.jiangtj.platform.test.cloud;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@JMicroCloudTest
@AutoConfigureMockMvc
@Import({JMicroCloudMvcConfiguration.class})
public @interface JMicroCloudMvcTest {
}
