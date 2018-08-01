package com.jtj.cloud.baseclient;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Created by MrTT (jiang.taojie@foxmail.com)
 * 2018/8/1.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CurlControllerTest {

    @Autowired
    private ApplicationContext context;

    private WebTestClient client;

    @Before
    public void setUp() {
        client = WebTestClient.bindToApplicationContext(context).build();
    }

    @Test
    public void get() {

        client.get().uri("/curl/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Your id is 1");

        client.get().uri("/curl?name=Jone Test")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Your QueryParams is {name=[Jone Test]}");

    }

    @Test
    public void post() {
        client.post().uri("/curl")
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(User.of("Jone Po",25,1))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(String.class).isEqualTo("Your Body is {\"name\":\"Jone Po\",\"age\":25,\"sex\":1}");
    }

    @Test
    public void put() {
        client.put().uri("/curl")
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(User.of("Jone Po",25,1))
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Your Body is {\"name\":\"Jone Po\",\"age\":25,\"sex\":1}");
    }

    @Test
    public void delete() {
        client.delete().uri("/curl").exchange().expectStatus().isNoContent();
    }
}