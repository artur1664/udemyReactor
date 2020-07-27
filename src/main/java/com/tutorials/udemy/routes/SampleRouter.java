package com.tutorials.udemy.routes;

import com.tutorials.udemy.routes.handlers.SampleHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class SampleRouter {

    @Bean
    public RouterFunction<ServerResponse> route(SampleHandler sampleHandler) {

        return RouterFunctions
                .route(GET("/router/flux").and(accept(MediaType.APPLICATION_JSON)), sampleHandler::flux)
                .andRoute(GET("/router/mono").and(accept(MediaType.APPLICATION_JSON)), sampleHandler::mono);
    }

}
