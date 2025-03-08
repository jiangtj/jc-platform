package com.jiangtj.platform.test;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@JMicroTest
@AutoConfigureMockMvc
@Import({JMicroWebTestClientMvcConfiguration.class})
public @interface JMicroMvcTest {
}
