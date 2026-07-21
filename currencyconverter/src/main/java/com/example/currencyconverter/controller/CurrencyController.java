package com.example.currencyconverter.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.currencyconverter.model.CurrencyLog;
import com.example.currencyconverter.service.CurrencyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/currency")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;

    @PostMapping("/convert")
    public CurrencyLog convertCurrency(
            @RequestHeader("X-API-KEY") String apiKey,
            @RequestParam double value,
            @RequestParam String currency
    ) {
        currencyService.validateApiKey(apiKey);
        return currencyService.convertAndSave(value, currency);
    }

    @GetMapping("/history")
    public List<CurrencyLog> getAllLogs(
            @RequestHeader("X-API-KEY") String apiKey
    ) {
        currencyService.validateApiKey(apiKey);
        return currencyService.getAllLogs();
    }
}