package com.tutorials.udemy.controllers;

import com.tutorials.udemy.SimpleMongoRepository;
import com.tutorials.udemy.domain.SimpleMongo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/v1/simple/mongo")
public class SimpleMongoController {

    private final SimpleMongoRepository simpleMongoRepository;

    public SimpleMongoController(SimpleMongoRepository simpleMongoRepository) {
        this.simpleMongoRepository = simpleMongoRepository;
    }

    @GetMapping
    Flux<SimpleMongo> getAll() {
        return simpleMongoRepository.findAll();
    }

    @GetMapping("/{id}")
    Mono<ResponseEntity<SimpleMongo>> getById(@PathVariable String id) {
        return simpleMongoRepository.findById(id).map(simpleMongo -> new ResponseEntity<>(simpleMongo, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    Mono<ResponseEntity<SimpleMongo>> addNew(@RequestBody SimpleMongo simpleMongo) {
        return simpleMongoRepository.save(simpleMongo).map(saved -> new ResponseEntity<>(saved, HttpStatus.CREATED));
    }
}
