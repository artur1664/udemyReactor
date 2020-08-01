package com.tutorials.udemy.routes.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class SampleHandler {

    public Mono<ServerResponse> flux(ServerRequest request) {
        request.queryParams().forEach((s, strings) -> log.info("query parameters from request object: {}, {}", s, strings));
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        Flux.just(1, 2, 3).log(), Integer.class
                );
    }

    public Mono<ServerResponse> mono(ServerRequest request) {
        request.queryParams().forEach((s, strings) -> log.info("query parameters from request object: {}, {}", s, strings));
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        Mono.just(1).log(), Integer.class
                );
    }
}
