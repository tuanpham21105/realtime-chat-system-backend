package com.owl.chat_service.external_service.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.owl.chat_service.external_service.dto.WsMessageDto;
import com.owl.chat_service.infrastructure.properties.ExternalServicesProperties;

import reactor.core.publisher.Mono;

@Component
public class WebSocketGatewayApiClient {
    @Value("${intenal-api-key}")
    private String internalApiKey;

    private final WebClient webClient;

    public WebSocketGatewayApiClient(WebClient.Builder builder, ExternalServicesProperties properties) {
        webClient = builder.baseUrl(properties.getWsgateway() + "/ws-message").build();
    }

    public void sendToUser(String userId, WsMessageDto message) {
        webClient.post()
            .uri("/user/{userId}", userId)
            .header("X-Internal-Api-Key", internalApiKey)
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
            .header("X-Internal-Api-Key", internalApiKey)
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
