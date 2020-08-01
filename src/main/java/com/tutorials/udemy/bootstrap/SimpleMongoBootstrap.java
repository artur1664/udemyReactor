package com.tutorials.udemy.bootstrap;

import com.tutorials.udemy.SimpleMongoRepository;
import com.tutorials.udemy.domain.SimpleMongo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
@Profile("!test")
public class SimpleMongoBootstrap implements CommandLineRunner {

    private final SimpleMongoRepository simpleMongoRepository;

    public SimpleMongoBootstrap(SimpleMongoRepository simpleMongoRepository) {
        this.simpleMongoRepository = simpleMongoRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        log.info("Bootstrap simple mongo data");

        simpleMongoRepository.deleteAll().thenMany(simpleMongoRepository.saveAll(Arrays.asList(
                new SimpleMongo(null, "value 1", "value2"),
                new SimpleMongo(null, "value 2", "value3"),
                new SimpleMongo(null, "value 4", "value5"),
                new SimpleMongo(null, "value 6", "value5"),
                new SimpleMongo(null, "value 7", "value8"),
                new SimpleMongo(null, "value 8", "value9")
        ))).log().subscribe();

    }
}
