package com.tutorials.udemy;

import com.tutorials.udemy.domain.SimpleMongo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimpleMongoRepository extends ReactiveMongoRepository<SimpleMongo, String> {
}
