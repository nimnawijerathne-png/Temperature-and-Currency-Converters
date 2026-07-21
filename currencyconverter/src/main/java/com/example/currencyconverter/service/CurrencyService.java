package com.example.currencyconverter.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.currencyconverter.exception.UnauthorizedException;
import com.example.currencyconverter.model.CurrencyLog;
import com.example.currencyconverter.repository.ApiKeyRepository;
import com.example.currencyconverter.repository.CurrencyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final ApiKeyRepository apiKeyRepository;

    // Fixed exchange rate for the lab (1 USD = 300 LKR). Adjust as needed.
    private static final double USD_TO_LKR_RATE = 300.0;

    public void validateApiKey(String requestKey) {
        if (requestKey == null || requestKey.trim().isEmpty()) {
            throw new UnauthorizedException("API Key missing from HTTP Headers!");
        }

        apiKeyRepository.findByKeyValueAndActiveTrue(requestKey.trim())
                .orElseThrow(() -> new UnauthorizedException("Invalid, inactive, or revoked API Key provided!"));
    }

    public CurrencyLog convertAndSave(double value, String currency) {
        double result;
        String outputCurrency;

        if ("USD".equalsIgnoreCase(currency)) {
            result = value * USD_TO_LKR_RATE;
            outputCurrency = "LKR";

        } else if ("LKR".equalsIgnoreCase(currency)) {
            result = value / USD_TO_LKR_RATE;
            outputCurrency = "USD";

        } else {
            throw new IllegalArgumentException("Unsupported currency: " + currency);
        }

        CurrencyLog log = new CurrencyLog();
        log.setInputAmount(value);
        log.setInputCurrency(currency.toUpperCase());
        log.setOutputAmount(result);
        log.setOutputCurrency(outputCurrency);
        log.setTimestamp(LocalDateTime.now().toString());

        return currencyRepository.save(log);
    }

    public List<CurrencyLog> getAllLogs() {
        return currencyRepository.findAll();
    }
}