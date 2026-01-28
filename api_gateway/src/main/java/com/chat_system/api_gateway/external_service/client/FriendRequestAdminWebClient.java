package com.chat_system.api_gateway.external_service.client;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.chat_system.api_gateway.external_service.dto.response.social_service.FriendRequestDto;
import com.chat_system.api_gateway.infrastructure.properties.WebClientProperties;
import com.chat_system.api_gateway.presentation.dto.request.friend_request.FriendRequestCreateRequest;
import com.chat_system.api_gateway.presentation.dto.request.friend_request.FriendRequestResponseRequest;

import reactor.core.publisher.Mono;

@Service
public class FriendRequestAdminWebClient {
    private final WebClient webClient;

    public FriendRequestAdminWebClient(WebClient.Builder builder, WebClientProperties properties) {
        this.webClient = builder.baseUrl(properties.social).build();
    }
    
    public List<FriendRequestDto> getFriendRequests(
        int page, int size, boolean ascSort,
        String keywords, String status,
        Instant createdStart, Instant createdEnd,
        Instant updatedStart, Instant updatedEnd
    ) {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/admin/friend-request")
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .queryParam("ascSort", ascSort)
                        .queryParamIfPresent("keywords", Optional.ofNullable(keywords))
                        .queryParamIfPresent("status", Optional.ofNullable(status))
                        .queryParamIfPresent("createdDateStart", Optional.ofNullable(createdStart))
                        .queryParamIfPresent("createdDateEnd", Optional.ofNullable(createdEnd))
                        .queryParamIfPresent("updatedDateStart", Optional.ofNullable(updatedStart))
                        .queryParamIfPresent("updatedDateEnd", Optional.ofNullable(updatedEnd))
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Friend request service error")))
                .bodyToMono(new ParameterizedTypeReference<List<FriendRequestDto>>() {})
                .block();
    }

    public FriendRequestDto getFriendRequestById(String id) {

        return webClient.get()
                .uri("/admin/friend-request/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Friend request service error")))
                .bodyToMono(FriendRequestDto.class)
                .block();
    }

    public List<FriendRequestDto> getBySenderId(
        String senderId, int page, int size, boolean ascSort,
        String keywords, String status,
        Instant createdStart, Instant createdEnd,
        Instant updatedStart, Instant updatedEnd
    ) {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/admin/friend-request/sender/{id}")
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .queryParam("ascSort", ascSort)
                        .queryParamIfPresent("keywords", Optional.ofNullable(keywords))
                        .queryParamIfPresent("status", Optional.ofNullable(status))
                        .queryParamIfPresent("createdDateStart", Optional.ofNullable(createdStart))
                        .queryParamIfPresent("createdDateEnd", Optional.ofNullable(createdEnd))
                        .queryParamIfPresent("updatedDateStart", Optional.ofNullable(updatedStart))
                        .queryParamIfPresent("updatedDateEnd", Optional.ofNullable(updatedEnd))
                        .build(senderId))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Friend request service error")))
                .bodyToMono(new ParameterizedTypeReference<List<FriendRequestDto>>() {})
                .block();
    }

    public List<FriendRequestDto> getByReceiverId(
        String receiverId, int page, int size, boolean ascSort,
        String keywords, String status,
        Instant createdStart, Instant createdEnd,
        Instant updatedStart, Instant updatedEnd
    ) {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/admin/friend-request/receiver/{id}")
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .queryParam("ascSort", ascSort)
                        .queryParamIfPresent("keywords", Optional.ofNullable(keywords))
                        .queryParamIfPresent("status", Optional.ofNullable(status))
                        .queryParamIfPresent("createdDateStart", Optional.ofNullable(createdStart))
                        .queryParamIfPresent("createdDateEnd", Optional.ofNullable(createdEnd))
                        .queryParamIfPresent("updatedDateStart", Optional.ofNullable(updatedStart))
                        .queryParamIfPresent("updatedDateEnd", Optional.ofNullable(updatedEnd))
                        .build(receiverId))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Friend request service error")))
                .bodyToMono(new ParameterizedTypeReference<List<FriendRequestDto>>() {})
                .block();
    }

    public List<FriendRequestDto> getByUserId(
        String userId, int page, int size, boolean ascSort,
        String keywords, String status,
        Instant createdStart, Instant createdEnd,
        Instant updatedStart, Instant updatedEnd
    ) {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/admin/friend-request/user/{id}")
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .queryParam("ascSort", ascSort)
                        .queryParamIfPresent("keywords", Optional.ofNullable(keywords))
                        .queryParamIfPresent("status", Optional.ofNullable(status))
                        .queryParamIfPresent("createdDateStart", Optional.ofNullable(createdStart))
                        .queryParamIfPresent("createdDateEnd", Optional.ofNullable(createdEnd))
                        .queryParamIfPresent("updatedDateStart", Optional.ofNullable(updatedStart))
                        .queryParamIfPresent("updatedDateEnd", Optional.ofNullable(updatedEnd))
                        .build(userId))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Friend request service error")))
                .bodyToMono(new ParameterizedTypeReference<List<FriendRequestDto>>() {})
                .block();
    }

    public List<FriendRequestDto> getByUsers(
        String firstUserId, String secondUserId,
        int page, int size, boolean ascSort,
        String keywords, String status,
        Instant createdStart, Instant createdEnd,
        Instant updatedStart, Instant updatedEnd
    ) {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/admin/friend-request/first-user/{f}/second-user/{s}")
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .queryParam("ascSort", ascSort)
                        .queryParamIfPresent("keywords", Optional.ofNullable(keywords))
                        .queryParamIfPresent("status", Optional.ofNullable(status))
                        .queryParamIfPresent("createdDateStart", Optional.ofNullable(createdStart))
                        .queryParamIfPresent("createdDateEnd", Optional.ofNullable(createdEnd))
                        .queryParamIfPresent("updatedDateStart", Optional.ofNullable(updatedStart))
                        .queryParamIfPresent("updatedDateEnd", Optional.ofNullable(updatedEnd))
                        .build(firstUserId, secondUserId))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Friend request service error")))
                .bodyToMono(new ParameterizedTypeReference<List<FriendRequestDto>>() {})
                .block();
    }

    public List<FriendRequestDto> getFromUserToUser(String senderId, String receiverId) {

        return webClient.get()
                .uri("/admin/friend-request/sender/{s}/receiver/{r}",
                        senderId, receiverId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Friend request service error")))
                .bodyToMono(new ParameterizedTypeReference<List<FriendRequestDto>>() {})
                .block();
    }

    public FriendRequestDto createFriendRequest(FriendRequestCreateRequest request) {

        return webClient.post()
                .uri("/admin/friend-request")
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Friend request service error")))
                .bodyToMono(FriendRequestDto.class)
                .block();
    }

    public FriendRequestDto updateStatus(
        String id,
        FriendRequestResponseRequest request) {

        return webClient.patch()
                .uri("/admin/friend-request/{id}/response", id)
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Friend request service error")))
                .bodyToMono(FriendRequestDto.class)
                .block();
    }

    public String deleteFriendRequest(String id) {

        return webClient.delete()
                .uri("/admin/friend-request/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Friend request service error")))
                .bodyToMono(String.class)
                .block();
    }

}
