package com.owl.social_service.external_service.client;

import java.time.Duration;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.owl.social_service.external_service.dto.ChatCreateRequestDto;
import com.owl.social_service.external_service.dto.ChatDto;
import com.owl.social_service.infrastructure.properties.WebClientProperties;

import reactor.core.publisher.Mono;

@Component
public class ChatServiceApiClient {
    private final WebClient webClient;

    public ChatServiceApiClient(WebClient.Builder builder, WebClientProperties webClientProperties) {
        webClient = builder.baseUrl(webClientProperties.getChat()).build();
    }

    public ChatDto createChat(String requesterId, ChatCreateRequestDto chatCreateRequestDto) {
        return webClient.post() // 1. Changed to POST for creation
            .uri("/chat")
            .header("requesterId", requesterId)
            .bodyValue(chatCreateRequestDto) // 2. This adds your DTO as the JSON body
            .exchangeToMono(response -> {
                if (response == null)
                    return Mono.empty();

                if (response.statusCode().is2xxSuccessful()) {
                    return response.bodyToMono(ChatDto.class);
                }

                if (response.statusCode().is4xxClientError()) {
                    // We read the body as a String (or a Map) to get the error message
                    return response.bodyToMono(String.class)
                        .flatMap(errorBody -> {
                            // If body is empty, use a default message
                            String msg = (errorBody != null && !errorBody.isBlank()) 
                                        ? errorBody : "Unknown Client Error";
                            return Mono.error(new RuntimeException("Chat error: " + msg));
                        });
                }

                if (response.statusCode().is5xxServerError())
                    return Mono.error(new RuntimeException("Server not found"));

                return Mono.empty();
            })
            .timeout(Duration.ofSeconds(3))
            .block(); // Blocks and returns ChatDto
    }
}
