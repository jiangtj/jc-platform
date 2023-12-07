package com.jiangtj.platform.test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ExtendWith(JMicroExtension.class)
@SpringBootTest
@AutoConfigureRestDocs
@Import({JMicroConfiguration.class})
@ActiveProfiles("testcase")
public @interface JMicroTest {
}
