package com.chat_system.api_gateway.external_service.client;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.chat_system.api_gateway.external_service.dto.response.social_service.FriendshipDto;
import com.chat_system.api_gateway.infrastructure.properties.WebClientProperties;

import reactor.core.publisher.Mono;

@Service
public class FriendshipUserWebClient {
    private final WebClient webClient;

    public FriendshipUserWebClient(WebClient.Builder builder, WebClientProperties properties) {
        this.webClient = builder.baseUrl(properties.user).build();
    }

    public List<FriendshipDto> getFriendships(
        String requesterId,
        int page,
        int size,
        boolean ascSort,
        Instant createdDateStart,
        Instant createdDateEnd
    ) {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/friendship")
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .queryParam("ascSort", ascSort)
                        .queryParamIfPresent("createdDateStart", Optional.ofNullable(createdDateStart))
                        .queryParamIfPresent("createdDateEnd", Optional.ofNullable(createdDateEnd))
                        .build())
                .header("requesterId", requesterId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Friendship service error")))
                .bodyToMono(new ParameterizedTypeReference<List<FriendshipDto>>() {})
                .block();
    }

    public FriendshipDto getFriendshipById(String requesterId, String id) {
        return webClient.get()
                .uri("/friendship/{id}", id)
                .header("requesterId", requesterId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Friendship service error")))
                .bodyToMono(FriendshipDto.class)
                .block();
    }

    public FriendshipDto getFriendshipWithUser(String requesterId, String userId) {
        return webClient.get()
                .uri("/friendship/user/{userId}", userId)
                .header("requesterId", requesterId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Friendship service error")))
                .bodyToMono(FriendshipDto.class)
                .block();
    }

    public String deleteFriendship(String requesterId, String id) {
        return webClient.delete()
                .uri("/friendship/{id}", id)
                .header("requesterId", requesterId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Friendship service error")))
                .bodyToMono(String.class)
                .block();
    }
}
