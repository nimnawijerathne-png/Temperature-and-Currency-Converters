package com.example.tempconverter.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "conversions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemperatureLog {

    @Id
    private String id;

    private double inputTemperature;
    private String inputUnit;
    private double outputTemperature;
    private String outputUnit;
    private String timestamp;
}