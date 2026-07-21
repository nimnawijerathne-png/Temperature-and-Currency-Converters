package com.example.tempconverter.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "api_keys")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiKey {

    @Id
    private String id;

    private String keyValue;    // The actual secret token string
    private String clientName;  // Descriptive identifier (e.g., "MobileAppClient")
    private boolean active;     // Quickly revoke access without deleting the document
}