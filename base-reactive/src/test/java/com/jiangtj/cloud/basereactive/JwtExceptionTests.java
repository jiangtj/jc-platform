package com.jiangtj.cloud.basereactive;

import com.jiangtj.cloud.auth.AuthServer;
import com.jiangtj.cloud.auth.TokenType;
import com.jiangtj.cloud.test.JCloudWebClientBuilder;
import com.jiangtj.cloud.test.JCloudWebTest;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;
import java.time.Duration;

import static com.jiangtj.cloud.auth.AuthRequestAttributes.TOKEN_HEADER_NAME;

@JCloudWebTest
class JwtExceptionTests {

    @Resource
    AuthServer authServer;

    @Test
    void testJwtException(JCloudWebClientBuilder client) {
        String token = authServer.builder()
            .setAuthType(TokenType.SYSTEM_USER)
            .setExpires(Duration.ofMinutes(50))
            .build();
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
        detail.setTitle("Invalid Token");
        detail.setDetail("ExpiresTime is bigger than max expires time!");
        detail.setInstance(URI.create("/needyoken"));
        client.build().get().uri("/needyoken")
            .header(TOKEN_HEADER_NAME, token)
            .exchange()
            .expectStatus().is4xxClientError()
            .expectBody(ProblemDetail.class).isEqualTo(detail);
    }
}
