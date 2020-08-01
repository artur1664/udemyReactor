package com.tutorials.udemy.routes;

import com.tutorials.udemy.routes.handlers.SampleHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

//use this (@WebFluxTest looks for @Controller or @RestController) so will not work without explicit context
//@AutoConfigureWebTestClient
//@SpringBootTest
//or this but specify context configuration for router and handler (this is better because dont try to connect to db - not loaded full context)
@ContextConfiguration(classes = {SampleRouter.class, SampleHandler.class})
@WebFluxTest
class SampleRouterTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testRouteFlux() {
        Flux<Integer> result = webTestClient.get()
                .uri("/router/flux")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Integer.class)
                .getResponseBody();

        StepVerifier.create(result)
                .expectSubscription()
                .expectNext(1)
                .expectNext(2)
                .expectNext(3)
                .verifyComplete();
    }
}