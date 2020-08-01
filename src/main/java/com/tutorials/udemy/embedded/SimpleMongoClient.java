package com.tutorials.udemy.embedded;

import com.tutorials.udemy.domain.SimpleMongo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class SimpleMongoClient {

    private final WebClient webClient;

    private String id = "5f21c2c79ff52c01377a7bf6";

    public SimpleMongoClient(@Value("${simple.client}") String host) {
        //should be in config file but :)
        this.webClient = WebClient.builder().baseUrl(host).build();
    }

    @Scheduled(fixedDelay = 10000)
    public void getAllTask() {
        log.info("=============================== get all ===============================");
        webClient.get().uri("/router/simple/mongo")
                .retrieve().bodyToFlux(SimpleMongo.class)
                .log().subscribe();
    }

    @Scheduled(fixedDelay = 11000)
    public void addNew() {
        log.info("=============================== add new ===============================");
        SimpleMongo newEntry = new SimpleMongo(null, "new value 1", "new value 2");

        webClient.post().uri("/router/simple/mongo")
                .body(BodyInserters.fromPublisher(Mono.just(newEntry), SimpleMongo.class))
                .retrieve().bodyToFlux(SimpleMongo.class)
                .log().subscribe(simpleMongo -> id = simpleMongo.getId());
    }

    @Scheduled(fixedDelay = 12000)
    public void getById() {
        log.info("=============================== get one by id ===============================");
        webClient.get().uri("/router/simple/mongo/{id}", id)
                .retrieve().bodyToFlux(SimpleMongo.class)
                .log().subscribe();
    }

    @Scheduled(fixedDelay = 13000)
    public void update() {
        log.info("=============================== update ===============================");
        SimpleMongo simpleMongo = new SimpleMongo(id, "some value 1 updated", "some value 2 updated");

        webClient.put().uri("/router/simple/mongo")
                .body(BodyInserters.fromPublisher(Mono.just(simpleMongo), SimpleMongo.class))
                .retrieve().bodyToFlux(SimpleMongo.class)
                .log().subscribe();
    }

    @Scheduled(fixedDelay = 14000)
    public void delete() {
        log.info("=============================== delete ===============================");

        webClient.delete().uri("/router/simple/mongo/{id}", id)
                .exchange()
                .log().subscribe();
    }
}
