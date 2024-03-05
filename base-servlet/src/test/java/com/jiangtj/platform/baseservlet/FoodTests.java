package com.jiangtj.platform.baseservlet;

import com.jiangtj.platform.test.cloud.JMicroCloudMvcTest;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertTrue;

@JMicroCloudMvcTest
public class FoodTests {

    @Resource
    WebTestClient webClient;

    @Test
    void testValid() {
        FoodController.Food food = new FoodController.Food("c", 1);
        webClient.post().uri("/food/create")
            .bodyValue(food)
            .exchange()
            .expectStatus().isOk()
            .expectBody(FoodController.Food.class).isEqualTo(food);
    }

    @Test
    void testInvalid() {
        FoodController.Food food = new FoodController.Food("  ", -1);
        webClient.post().uri("/food/create")
            .bodyValue(food)
            .exchange()
            .expectStatus().is4xxClientError()
            .expectAll(responseSpec -> {
                responseSpec.expectBody()
                    .jsonPath("type").exists()
                    .jsonPath("status").isEqualTo(HttpStatus.BAD_REQUEST.value())
                    .jsonPath("title").isEqualTo("Invalid request content")
                    .jsonPath("detail").value(d -> {
                        assertTrue(d.contains("name: must not be blank"));
                        assertTrue(d.contains("price: must be greater than or equal to 0"));
                    }, String.class)
                    .jsonPath("instance").exists();
            });
    }

}
