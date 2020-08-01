package com.tutorials.udemy.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
public class IntegerController {

    //in this case client will wait for all elements to be emitted. So if delay between elements is 1 sec client will have to wait for 5 sec for response
    @GetMapping("/flux")
    public Flux<Integer> getSimpleFluxOfIntegers() {
        return Flux.just(1, 2, 3, 4, 5).delayElements(Duration.ofSeconds(1)).log();
    }

    //in this case controller produces response as stream, so client will get new value ass soon ass onNext() triggers
    @GetMapping(value = "/flux/stream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Integer> getStreamFluxOfIntegers() {
        return Flux.just(1, 2, 3, 4, 5).delayElements(Duration.ofSeconds(1)).log();
    }

    //pull data approach, values will be emitted until cancel()
    @GetMapping(value = "/flux/infinite", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Long> getInfiniteStreamFluxOfIntegers() {
        return Flux.interval(Duration.ofSeconds(1L)).log();
    }
}
