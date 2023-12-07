package com.jiangtj.platform.test.cloud;

import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@JMicroCloudTest
@AutoConfigureWebTestClient
public @interface JMicroCloudFluxTest {
}
