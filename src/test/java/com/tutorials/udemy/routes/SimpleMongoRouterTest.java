package com.tutorials.udemy.routes;

import com.tutorials.udemy.SimpleMongoRepository;
import com.tutorials.udemy.domain.SimpleMongo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureWebTestClient
//@DirtiesContext create new context for each test case
@DirtiesContext
@ActiveProfiles("test")
class SimpleMongoRouterTest {

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
        webTestClient.get().uri("/router/simple/mongo")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(SimpleMongo.class)
                .hasSize(6);
    }

    //consumeWith allows to get response from request
    @Test
    public void getAll_approach2() {
        webTestClient.get().uri("/router/simple/mongo")
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

    @Test
    public void getById() {
        webTestClient.get().uri("/router/simple/mongo/{id}", "1234")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.field1", "value 8");
    }

    @Test
    public void getByIdNotFound() {
        webTestClient.get().uri("/router/simple/mongo/{id}", "987")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void updateEntity() {
        SimpleMongo updated = new SimpleMongo("1234", "new val 1", "new val 2");

        webTestClient.put().uri("/router/simple/mongo")
                .body(Mono.just(updated), SimpleMongo.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(SimpleMongo.class)
                .consumeWith(simpleMongoEntityExchangeResult -> Optional.ofNullable(simpleMongoEntityExchangeResult.getResponseBody())
                        .map(simpleMongo -> {
                            assertEquals(simpleMongo.getField1(), "new val 1");
                            assertEquals(simpleMongo.getField2(), "new val 2");
                            assertEquals(simpleMongo.getId(), "1234");
                            return simpleMongo;
                        }));
    }

    @Test
    public void addNewEntity() {
        SimpleMongo newEntity = new SimpleMongo(null, "new entity 1", "new entity 2");

        webTestClient.post().uri("/router/simple/mongo")
                .body(Mono.just(newEntity), SimpleMongo.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(SimpleMongo.class)
                .consumeWith(simpleMongoEntityExchangeResult -> Optional.ofNullable(simpleMongoEntityExchangeResult.getResponseBody()).
                        map(simpleMongo -> {
                            assertEquals(simpleMongo.getField1(), "new entity 1");
                            assertEquals(simpleMongo.getField2(), "new entity 2");
                            assertNotNull(simpleMongo.getId());
                            return simpleMongo;
                        }));
    }

    @Test
    public void deleteByIdEntity() {

        webTestClient.delete().uri("/router/simple/mongo/{id}", "1234")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(SimpleMongo.class);

        Mono<SimpleMongo> result = simpleMongoRepository.findById("1234");
        result.subscribe(Assertions::assertNull);
    }
}