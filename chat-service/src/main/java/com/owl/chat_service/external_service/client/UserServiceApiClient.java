package com.owl.chat_service.external_service.client;

import java.time.Duration;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.owl.chat_service.external_service.dto.UserProfileDto;
import com.owl.chat_service.infrastructure.properties.ExternalServicesURL;

import reactor.core.publisher.Mono;

@Component
public class UserServiceApiClient {
    private final WebClient webClient;

    public UserServiceApiClient(WebClient.Builder builder, ExternalServicesURL properties) {
        webClient = builder.baseUrl(properties.getUser()).build();
    }

    public UserProfileDto getUserById(String id) {
        return webClient.get().uri("/user/{id}", id)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError,
                response -> Mono.error(new RuntimeException("User not found")))
            .onStatus(HttpStatusCode::is5xxServerError,
                response -> Mono.error(new RuntimeException("External server error")))
            .bodyToMono(UserProfileDto.class)
            .timeout(Duration.ofSeconds(3))
            .block();
    }
}
