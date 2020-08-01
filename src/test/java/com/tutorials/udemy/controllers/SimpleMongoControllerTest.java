package com.tutorials.udemy.controllers;

import com.tutorials.udemy.SimpleMongoRepository;
import com.tutorials.udemy.domain.SimpleMongo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureWebTestClient
//@DirtiesContext create new context for each test case
@DirtiesContext
@ActiveProfiles("test")
class SimpleMongoControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private SimpleMongoRepository simpleMongoRepository;

    @BeforeEach
    void setup() {
        simpleMongoRepository.deleteAll().thenMany(simpleMongoRepository.saveAll(Arrays.asList(
                new SimpleMongo(null, "value 1", "value2"),
                new SimpleMongo(null, "value 2", "value3"),
                new SimpleMongo(null, "value 4", "value5"),
                new SimpleMongo(null, "value 6", "value5"),
                new SimpleMongo(null, "value 7", "value8"),
                new SimpleMongo("1234", "value 8", "value9")
        ))).log().blockLast();
    }

    @Test
    public void getAll() {
        webTestClient.get().uri("/api/v1/simple/mongo")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(SimpleMongo.class)
                .hasSize(6);
    }

    //consumeWith allows to get response from request
    @Test
    public void getAll_approach2() {
        webTestClient.get().uri("/api/v1/simple/mongo")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(SimpleMongo.class)
                .hasSize(6)
                .consumeWith(listEntityExchangeResult -> Optional.ofNullable(listEntityExchangeResult.getResponseBody()).map(mongoList -> {
                    mongoList.forEach(simpleMongo -> assertNotNull(simpleMongo.getId()));
                    return mongoList;
                }));
    }

    //returnResult allows to get request result and assign it to variable
    @Test
    public void getAll_approach3() {
        Flux<SimpleMongo> result = webTestClient.get().uri("/api/v1/simple/mongo")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(SimpleMongo.class)
                .getResponseBody();

        StepVerifier.create(result)
                .expectNextCount(6)
                .verifyComplete();
    }

    @Test
    public void getById() {
        webTestClient.get().uri("/api/v1/simple/mongo/{id}", "1234")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.field1", "value 8");
    }

    @Test
    public void getByIdNotFound() {
        webTestClient.get().uri("/api/v1/simple/mongo/{id}", "987")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void addNew() {
        SimpleMongo simpleMongo = new SimpleMongo(null, "val 1", "val 2");

        webTestClient.post().uri("/api/v1/simple/mongo")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(simpleMongo), SimpleMongo.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.field1").isEqualTo("val 1")
                .jsonPath("$.field2").isEqualTo("val 2");
    }
}