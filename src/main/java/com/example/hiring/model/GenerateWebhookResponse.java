package com.example.hiring.model;

import lombok.Data;

@Data
public class GenerateWebhookResponse {
    // Adjust field names to match exact API; these are common choices.
    private String webhook;      // Submit URL returned by API
    private String accessToken;  // JWT for Authorization header
    private String message;      // optional
}
