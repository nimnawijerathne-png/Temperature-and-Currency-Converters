package com.example.tempconverter.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.tempconverter.model.TemperatureLog;
import com.example.tempconverter.service.TemperatureService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/temperatures")
@RequiredArgsConstructor
public class TemperatureController {

    private final TemperatureService temperatureService;

    @PostMapping("/convert")
    public TemperatureLog convertTemperature(
            @RequestHeader("X-API-KEY") String apiKey, // Captures key from HTTP Headers
            @RequestParam double value,
            @RequestParam String unit
    ) {
        // Enforce the database cross-reference check
        temperatureService.validateApiKey(apiKey);

        return temperatureService.convertAndSave(value, unit);
    }

    @GetMapping("/history")
    public List<TemperatureLog> getAllLogs(
            @RequestHeader("X-API-KEY") String apiKey // Secures history viewing as well
    ) {
        temperatureService.validateApiKey(apiKey);
        return temperatureService.getAllLogs();
    }

    @GetMapping("/safety-check")
    public String checkTemperatureSafety(@RequestParam double value,
                                          @RequestParam String unit) {
        return temperatureService.getSafetyWarning(value, unit);
    }

    @GetMapping("/history/filter")
    public List<TemperatureLog> getFilteredLogs(@RequestParam String unit) {
        return temperatureService.getLogsByUnit(unit);
    }

    @GetMapping("/history/above")
    public List<TemperatureLog> getLogsAboveThreshold(@RequestParam double threshold) {
        return temperatureService.getLogsAboveThreshold(threshold);
    }
}