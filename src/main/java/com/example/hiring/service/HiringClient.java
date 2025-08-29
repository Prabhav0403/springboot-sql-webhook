package com.example.hiring.service;

import com.example.hiring.model.GenerateWebhookRequest;
import com.example.hiring.model.GenerateWebhookResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class HiringClient {

    private final WebClient webClient;

    @Value("${app.endpoints.generateWebhook}")
    private String generateWebhookUrl;

    @Value("${app.endpoints.submitAnswer}")
    private String submitAnswerUrl;

    public Mono<GenerateWebhookResponse> generateWebhook(GenerateWebhookRequest req) {
        return webClient.post()
                .uri(generateWebhookUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(req)
                .retrieve()
                .bodyToMono(GenerateWebhookResponse.class);
    }

    public Mono<String> submitFinalQuery(String jwt, String finalQuery) {
        return webClient.post()
                .uri(submitAnswerUrl)
                .header(HttpHeaders.AUTHORIZATION, jwt) // If 401, try "Bearer " + jwt
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new FinalQueryPayload(finalQuery))
                .retrieve()
                .bodyToMono(String.class);
    }

    // local record for request body
    private record FinalQueryPayload(String finalQuery) {}
}
