package com.owl.social_service.external_service.client;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.owl.social_service.external_service.dto.WsMessageDto;
import com.owl.social_service.infrastructure.properties.WebClientProperties;

import reactor.core.publisher.Mono;

@Component
public class WebSocketGatewayApiClient {
    private final WebClient webClient;

    public WebSocketGatewayApiClient(WebClient.Builder builder, WebClientProperties properties) {
        webClient = builder.baseUrl(properties.getWsgateway() + "/ws-message").build();
    }

    public void sendToUser(String userId, WsMessageDto message) {
        webClient.post()
                .uri("/user/{userId}", userId)
                .bodyValue(message)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(errorMsg ->
                                        Mono.error(new RuntimeException(
                                                "Failed to send WS message: " + errorMsg
                                        ))
                                )
                )
                .toBodilessEntity()
                .block();
    }

    public void sendToChat(String chatId, WsMessageDto message) {
        webClient.post()
                .uri("/chat/{chatId}", chatId)
                .bodyValue(message)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(errorMsg ->
                                        Mono.error(new RuntimeException(
                                                "Failed to send WS message: " + errorMsg
                                        ))
                                )
                )
                .toBodilessEntity()
                .block();
    }
}
