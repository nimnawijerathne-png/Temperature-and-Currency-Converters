package com.example.currencyconverter.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.currencyconverter.model.CurrencyLog;

@Repository
public interface CurrencyRepository extends MongoRepository<CurrencyLog, String> {
    // Empty — inherits save(), findAll(), etc.
}