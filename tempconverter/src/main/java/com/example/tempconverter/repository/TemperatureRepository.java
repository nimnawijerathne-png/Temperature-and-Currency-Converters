package com.example.tempconverter.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.tempconverter.model.TemperatureLog;

@Repository
public interface TemperatureRepository extends MongoRepository<TemperatureLog, String> {

    // Spring Boot auto-generates a case-insensitive match query against 'inputUnit'
    List<TemperatureLog> findByInputUnitIgnoreCase(String inputUnit);

    // Bonus (Self-Study Question): finds logs where inputTemperature is strictly greater than the given value
    List<TemperatureLog> findByInputTemperatureGreaterThan(double threshold);
}