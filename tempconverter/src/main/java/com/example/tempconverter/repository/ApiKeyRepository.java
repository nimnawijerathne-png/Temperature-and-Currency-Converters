package com.example.tempconverter.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.tempconverter.model.ApiKey;

@Repository
public interface ApiKeyRepository extends MongoRepository<ApiKey, String> {
    // Spring Boot auto-generates a query to find the key AND verify it is active
    Optional<ApiKey> findByKeyValueAndActiveTrue(String keyValue);
}