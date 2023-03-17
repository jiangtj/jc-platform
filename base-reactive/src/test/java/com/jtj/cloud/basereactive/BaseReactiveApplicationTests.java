package com.jtj.cloud.basereactive;

import com.jtj.cloud.auth.AuthServer;
import com.jtj.cloud.auth.TokenType;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Duration;

class BaseReactiveApplicationTests extends AbstractServerTests {

    @Resource
    WebTestClient webClient;
    @Resource
    AuthServer authServer;

    @Test
    void testIndex() {
        String token = authServer.builder()
            .setAuthType(TokenType.SERVER)
            .setAudience(authServer.getApplicationName())
            .build();
        webClient.get().uri("/")
            .header(authServer.getProperties().getHeaderName(), token)
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class).isEqualTo("Base Reactive Client Started !!");
    }

    @Test
    void testErr() {
        String token = authServer.builder()
            .setAuthType(TokenType.SERVER)
            .setAudience(authServer.getApplicationName())
            .build();
        webClient.get().uri("/err")
            .header(authServer.getProperties().getHeaderName(), token)
            .exchange()
            .expectStatus().is4xxClientError();
    }

    @Test
    void testErr2() {
        String token = authServer.builder()
            .setAuthType(TokenType.SERVER)
            .setExpires(Duration.ofMinutes(11))
            .setAudience(authServer.getApplicationName())
            .build();
        webClient.get().uri("/err")
            .header(authServer.getProperties().getHeaderName(), token)
            .exchange()
            .expectStatus().is4xxClientError();
    }

    @Test
    void testNonToken() {
        webClient.get().uri("/")
            .exchange()
            .expectStatus().is4xxClientError();
    }

}
