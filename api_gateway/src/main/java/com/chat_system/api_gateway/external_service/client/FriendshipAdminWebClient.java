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
import com.chat_system.api_gateway.presentation.dto.request.friendship.FriendshipCreateRequest;

import reactor.core.publisher.Mono;

@Service
public class FriendshipAdminWebClient {
    private final WebClient webClient;

    public FriendshipAdminWebClient(WebClient.Builder builder, WebClientProperties properties) {
        this.webClient = builder.baseUrl(properties.social).build();
    }

    public List<FriendshipDto> getFriendships(
        int page,
        int size,
        boolean ascSort,
        String keywords,
        Instant createdDateStart,
        Instant createdDateEnd
    ) {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/admin/friendship")
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .queryParam("ascSort", ascSort)
                        .queryParamIfPresent("keywords", Optional.ofNullable(keywords))
                        .queryParamIfPresent("createdDateStart", Optional.ofNullable(createdDateStart))
                        .queryParamIfPresent("createdDateEnd", Optional.ofNullable(createdDateEnd))
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Friendship service error")))
                .bodyToMono(new ParameterizedTypeReference<List<FriendshipDto>>() {})
                .block();
    }

    public List<FriendshipDto> getFriendshipsByUserId(
        String userId,
        int page,
        int size,
        boolean ascSort,
        String keywords,
        Instant createdDateStart,
        Instant createdDateEnd
    ) {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/admin/friendship/user/{userId}")
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .queryParam("ascSort", ascSort)
                        .queryParamIfPresent("keywords", Optional.ofNullable(keywords))
                        .queryParamIfPresent("createdDateStart", Optional.ofNullable(createdDateStart))
                        .queryParamIfPresent("createdDateEnd", Optional.ofNullable(createdDateEnd))
                        .build(userId))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Friendship service error")))
                .bodyToMono(new ParameterizedTypeReference<List<FriendshipDto>>() {})
                .block();
    }

    public FriendshipDto getFriendshipByUsersId(
        String firstUserId,
        String secondUserId
    ) {

        return webClient.get()
                .uri("/admin/friendship/first-user/{f}/second-user/{s}",
                        firstUserId, secondUserId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Friendship service error")))
                .bodyToMono(FriendshipDto.class)
                .block();
    }

    public FriendshipDto getFriendshipById(String id) {
        return webClient.get()
                .uri("/admin/friendship/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Friendship service error")))
                .bodyToMono(FriendshipDto.class)
                .block();
    }

    public FriendshipDto createFriendship(FriendshipCreateRequest request) {

        return webClient.post()
                .uri("/admin/friendship")
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Friendship service error")))
                .bodyToMono(FriendshipDto.class)
                .block();
    }

    public String deleteFriendship(String id) {

        return webClient.delete()
                .uri("/admin/friendship/{id}", id)
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
