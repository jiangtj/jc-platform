package com.jtj.cloud.system;

import com.jtj.cloud.test.JCloudWebClientBuilder;
import com.jtj.cloud.test.JCloudWebTest;
import org.junit.jupiter.api.Test;

@JCloudWebTest
class RouterTest {

    @Test
    void testGetIndex(JCloudWebClientBuilder client) {
        client.build().get().uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("System Client Started !!");
    }

    /*@Test
    void testErrorToken(JCloudWebClientBuilder client) {
        client.build().mutateWith(new NoAuthWebTestClientConfigurer())
                .get().uri("/anypath")
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody(ErrorResult.class)
                .value(result -> {
                    assertEquals(HttpStatus.UNAUTHORIZED, result.getStatus());
                    assertEquals("无效的token！", result.getMessage());
                });
    }

    @Test
    void testGetStaticContent() {
        webClient.get().uri("/resources/dd.jpg")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().cacheControl(CacheControl.maxAge(365, TimeUnit.DAYS));

        webClient.get().uri("/resources/dd2.jpg")
                .exchange()
                .expectStatus().isNotFound();
    }*/
}
