package com.chat_system.websocket_gateway.external_service.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.chat_system.websocket_gateway.external_service.dto.ValidateAccessTokenResponseDto;

import reactor.core.publisher.Mono;

@Component
public class ValidateTokenWebClient {

    private final WebClient webClient;

    public ValidateTokenWebClient(WebClient.Builder builder, @Value("${external.service.api.user}") String baseUrl) {
        this.webClient = builder.baseUrl(baseUrl + "/token").build();
    }

    public ValidateAccessTokenResponseDto validateAccessToken(String accessToken) {

        return webClient.post()
            .uri("/access")
            .cookie("accessToken", accessToken)
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                response -> response.bodyToMono(String.class)
                    .flatMap(msg -> Mono.error(new RuntimeException("Unauthorized: " + msg)))
            )
            .onStatus(
                HttpStatusCode::is5xxServerError,
                response -> response.bodyToMono(String.class)
                    .flatMap(msg -> Mono.error(new RuntimeException("Server error: " + msg)))
            )
            .bodyToMono(ValidateAccessTokenResponseDto.class)
            .block();
    }
}
