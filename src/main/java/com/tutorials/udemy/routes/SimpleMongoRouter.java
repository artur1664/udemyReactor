package com.tutorials.udemy.routes;

import com.tutorials.udemy.routes.handlers.SimpleMongoHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class SimpleMongoRouter {

    @Bean
    public RouterFunction<ServerResponse> simpleMongoRoute(SimpleMongoHandler simpleMongoHandler) {

        return RouterFunctions
                .route(GET("/router/simple/mongo").and(accept(MediaType.APPLICATION_JSON)), simpleMongoHandler::getAll)
                .andRoute(GET("/router/simple/mongo/{id}").and(accept(MediaType.APPLICATION_JSON)), simpleMongoHandler::getById)
                .andRoute(POST("/router/simple/mongo").and(accept(MediaType.APPLICATION_JSON)), simpleMongoHandler::addNew)
                .andRoute(PUT("/router/simple/mongo").and(accept(MediaType.APPLICATION_JSON)), simpleMongoHandler::update)
                .andRoute(DELETE("/router/simple/mongo/{id}").and(accept(MediaType.APPLICATION_JSON)), simpleMongoHandler::deleteById);
    }
}
