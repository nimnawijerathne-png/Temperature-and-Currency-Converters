package com.example.tempconverter.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.tempconverter.exception.UnauthorizedException;
import com.example.tempconverter.model.TemperatureLog;
import com.example.tempconverter.repository.ApiKeyRepository;
import com.example.tempconverter.repository.TemperatureRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TemperatureService {

    private final TemperatureRepository temperatureRepository;
    private final ApiKeyRepository apiKeyRepository; // INJECT THE NEW REPOSITORY

    // Validates the incoming API key against the database
    public void validateApiKey(String requestKey) {
        if (requestKey == null || requestKey.trim().isEmpty()) {
            throw new UnauthorizedException("API Key missing from HTTP Headers!");
        }

        // Search the database for an active matching key token
        apiKeyRepository.findByKeyValueAndActiveTrue(requestKey.trim())
                .orElseThrow(() -> new UnauthorizedException("Invalid, inactive, or revoked API Key provided!"));
    }

    public TemperatureLog convertAndSave(double value, String unit) {
        double result;
        String outputUnit;

        if ("CELSIUS".equalsIgnoreCase(unit)) {
            result = (value * 9.0 / 5.0) + 32;
            outputUnit = "FAHRENHEIT";

        } else if ("FAHRENHEIT".equalsIgnoreCase(unit)) {
            result = (value - 32) * 5.0 / 9.0;
            outputUnit = "CELSIUS";

        } else {
            throw new IllegalArgumentException("Unsupported unit: " + unit);
        }

        TemperatureLog log = new TemperatureLog();
        log.setInputTemperature(value);
        log.setInputUnit(unit);
        log.setOutputTemperature(result);
        log.setOutputUnit(outputUnit);
        log.setTimestamp(LocalDateTime.now().toString());

        return temperatureRepository.save(log);
    }

    public List<TemperatureLog> getAllLogs() {
        return temperatureRepository.findAll();
    }

    public String getSafetyWarning(double value, String unit) {
        String cleanUnit = unit.trim().toUpperCase();
        double celsiusTemp = value;

        if ("FAHRENHEIT".equals(cleanUnit) || "F".equals(cleanUnit)) {
            celsiusTemp = (value - 32) * 5 / 9;
        }

        if (celsiusTemp >= 38.0) {
            return "Warning: " + value + "°" + cleanUnit + " is dangerously HOT! Stay hydrated.";
        } else if (celsiusTemp <= 0.0) {
            return "Warning: " + value + "°" + cleanUnit + " is freezing cold! Bundle up.";
        } else {
            return "The temperature is comfortable and safe.";
        }
    }

    public List<TemperatureLog> getLogsByUnit(String unit) {
        return temperatureRepository.findByInputUnitIgnoreCase(unit.trim());
    }

    public List<TemperatureLog> getLogsAboveThreshold(double threshold) {
        return temperatureRepository.findByInputTemperatureGreaterThan(threshold);
    }
}