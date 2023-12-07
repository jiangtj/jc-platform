package com.jiangtj.platform.test.cloud;

import com.jiangtj.platform.test.JMicroTest;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@JMicroTest
@Import({JMicroCloudConfiguration.class})
public @interface JMicroCloudTest {
}
