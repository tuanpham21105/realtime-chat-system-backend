package com.chat_system.api_gateway.external_service.client;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import com.chat_system.api_gateway.external_service.dto.response.social_service.FriendRequestDto;
import com.chat_system.api_gateway.infrastructure.properties.WebClientProperties;
import com.chat_system.api_gateway.presentation.dto.request.friend_request.FriendRequestCreateUserRequest;
import com.chat_system.api_gateway.presentation.dto.request.friend_request.FriendRequestResponseRequest;

import reactor.core.publisher.Mono;

@Service
public class FriendRequestUserWebClient {
    private final WebClient webClient;

    public FriendRequestUserWebClient(WebClient.Builder builder, WebClientProperties properties) {
        this.webClient = builder.baseUrl(properties.social).build();
    }
    
    private UriBuilder buildCommonParams(
            UriBuilder b,
            int page, int size, boolean ascSort,
            String keywords, String status,
            Instant createdStart, Instant createdEnd,
            Instant updatedStart, Instant updatedEnd) {

        return b.queryParam("page", page)
                .queryParam("size", size)
                .queryParam("ascSort", ascSort)
                .queryParamIfPresent("keywords", Optional.ofNullable(keywords))
                .queryParamIfPresent("status", Optional.ofNullable(status))
                .queryParamIfPresent("createdDateStart", Optional.ofNullable(createdStart))
                .queryParamIfPresent("createdDateEnd", Optional.ofNullable(createdEnd))
                .queryParamIfPresent("updatedDateStart", Optional.ofNullable(updatedStart))
                .queryParamIfPresent("updatedDateEnd", Optional.ofNullable(updatedEnd));
    }

    public List<FriendRequestDto> getFriendRequests(
            String requesterId,
            int page, int size, boolean ascSort,
            String keywords, String status,
            Instant createdStart, Instant createdEnd,
            Instant updatedStart, Instant updatedEnd) {

        return webClient.get()
                .uri(b -> buildCommonParams(
                        b.path("/friend-request"),
                        page, size, ascSort,
                        keywords, status,
                        createdStart, createdEnd,
                        updatedStart, updatedEnd).build())
                .header("requesterId", requesterId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Friend request service error")))
                .bodyToMono(new ParameterizedTypeReference<List<FriendRequestDto>>() {})
                .block();
    }

    public FriendRequestDto getById(String requesterId, String id) {

        return webClient.get()
                .uri("/friend-request/{id}", id)
                .header("requesterId", requesterId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Friend request service error")))
                .bodyToMono(FriendRequestDto.class)
                .block();
    }

    public List<FriendRequestDto> getSent(
        String requesterId,
        int page, int size, boolean ascSort,
        String keywords, String status,
        Instant cStart, Instant cEnd,
        Instant uStart, Instant uEnd) {

        return webClient.get()
                .uri(b -> buildCommonParams(
                        b.path("/friend-request/send"),
                        page, size, ascSort,
                        keywords, status,
                        cStart, cEnd,
                        uStart, uEnd).build())
                .header("requesterId", requesterId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Friend request service error")))
                .bodyToMono(new ParameterizedTypeReference<List<FriendRequestDto>>() {})
                .block();
    }

    public List<FriendRequestDto> getReceived(
        String requesterId,
        int page, int size, boolean ascSort,
        String keywords, String status,
        Instant cStart, Instant cEnd,
        Instant uStart, Instant uEnd) {

        return webClient.get()
                .uri(b -> buildCommonParams(
                        b.path("/friend-request/receive"),
                        page, size, ascSort,
                        keywords, status,
                        cStart, cEnd,
                        uStart, uEnd).build())
                .header("requesterId", requesterId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Friend request service error")))
                .bodyToMono(new ParameterizedTypeReference<List<FriendRequestDto>>() {})
                .block();
    }

    public List<FriendRequestDto> getWithUser(
            String requesterId, String userId,
            int page, int size, boolean ascSort,
            String keywords, String status,
            Instant cStart, Instant cEnd,
            Instant uStart, Instant uEnd) {

        return webClient.get()
                .uri(b -> buildCommonParams(
                        b.path("/friend-request/user/{id}"),
                        page, size, ascSort,
                        keywords, status,
                        cStart, cEnd,
                        uStart, uEnd).build(userId))
                .header("requesterId", requesterId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Friend request service error")))
                .bodyToMono(new ParameterizedTypeReference<List<FriendRequestDto>>() {})
                .block();
    }

    public FriendRequestDto getFromRequesterToUser(
        String requesterId, String receiverId) {

        return webClient.get()
                .uri("/friend-request/receiver/{id}", receiverId)
                .header("requesterId", requesterId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Friend request service error")))
                .bodyToMono(FriendRequestDto.class)
                .block();
    }

    public FriendRequestDto getFromUserToRequester(
        String requesterId, String senderId) {

        return webClient.get()
                .uri("/friend-request/sender/{id}", senderId)
                .header("requesterId", requesterId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Friend request service error")))
                .bodyToMono(FriendRequestDto.class)
                .block();
    }

    public FriendRequestDto create(
        String requesterId,
        FriendRequestCreateUserRequest request) {

        return webClient.post()
                .uri("/friend-request")
                .header("requesterId", requesterId)
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

    public FriendRequestDto respond(
        String requesterId,
        String id,
        FriendRequestResponseRequest request) {

        return webClient.patch()
                .uri("/friend-request/{id}/response", id)
                .header("requesterId", requesterId)
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

    public String delete(String requesterId, String id) {

        return webClient.delete()
                .uri("/friend-request/{id}", id)
                .header("requesterId", requesterId)
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
