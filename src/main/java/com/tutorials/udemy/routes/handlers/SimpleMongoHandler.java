package com.tutorials.udemy.routes.handlers;

import com.tutorials.udemy.SimpleMongoRepository;
import com.tutorials.udemy.domain.SimpleMongo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;


@Slf4j
@Component
public class SimpleMongoHandler {

    private final SimpleMongoRepository simpleMongoRepository;

    public SimpleMongoHandler(SimpleMongoRepository simpleMongoRepository) {
        this.simpleMongoRepository = simpleMongoRepository;
    }

    public Mono<ServerResponse> getAll(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(simpleMongoRepository.findAll(), SimpleMongo.class);
    }

    public Mono<ServerResponse> getById(ServerRequest serverRequest) {

        return simpleMongoRepository.findById(serverRequest.pathVariable("id")).flatMap(simpleMongo ->
                ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(simpleMongo)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> addNew(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(SimpleMongo.class).flatMap(simpleMongo ->
                ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(simpleMongoRepository.save(simpleMongo), SimpleMongo.class));
    }

    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(SimpleMongo.class).flatMap(simpleMongo ->
                ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(simpleMongoRepository.save(simpleMongo), SimpleMongo.class));
    }

    public Mono<ServerResponse> deleteById(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(simpleMongoRepository.deleteById(serverRequest.pathVariable("id")), SimpleMongo.class);
    }
}
