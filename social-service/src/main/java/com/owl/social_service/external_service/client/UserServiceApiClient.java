package com.owl.social_service.external_service.client;

import java.time.Duration;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.owl.social_service.external_service.dto.UserProfileDto;
import com.owl.social_service.infrastructure.properties.WebClientProperties;

import reactor.core.publisher.Mono;

@Component
public class UserServiceApiClient {
    private final WebClient webClient;

    public UserServiceApiClient(WebClient.Builder builder, WebClientProperties properties) {
        webClient = builder.baseUrl(properties.getUser()).build();
    }

    public UserProfileDto getUserById(String id) {
        return webClient.get().uri("/user/" + id)
            .exchangeToMono(response -> {
                if (response == null)
                    return Mono.empty();

                if (response.statusCode().is2xxSuccessful()) {
                    // If body is empty this will become an empty Mono -> block() returns null
                    return response.bodyToMono(UserProfileDto.class);
                }
                if (response.statusCode().value() == 404) {
                    // convert 404 into empty result
                    return Mono.empty();
                }
                // propagate other errors as exceptions
                if (response.statusCode().is4xxClientError())
                    throw new RuntimeException("User not found");

                if (response.statusCode().is5xxServerError())
                    throw new RuntimeException("Server not found");

                return Mono.empty();
            })
            .timeout(Duration.ofSeconds(3))
            .block();
    }
}
