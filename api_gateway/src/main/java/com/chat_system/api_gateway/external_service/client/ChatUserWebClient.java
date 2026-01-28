package com.chat_system.api_gateway.external_service.client;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.chat_system.api_gateway.external_service.dto.response.chat_service.ChatDto;
import com.chat_system.api_gateway.infrastructure.properties.WebClientProperties;
import com.chat_system.api_gateway.presentation.dto.request.chat.ChatUpdateNameRequest;
import com.chat_system.api_gateway.presentation.dto.request.chat.ChatUserRequest;

import reactor.core.publisher.Mono;

@Service
public class ChatUserWebClient {
    private final WebClient webClient;

    public ChatUserWebClient(WebClient.Builder builder, WebClientProperties properties) {
        this.webClient = builder.baseUrl(properties.chat).build();
    }

    public List<ChatDto> getChatsByMember(
        String requesterId,
        String keywords,
        int page,
        int size,
        boolean ascSort,
        String type,
        Instant joinDateStart,
        Instant joinDateEnd
    ) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/member")
                        .queryParam("keywords", keywords)
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .queryParam("ascSort", ascSort)
                        .queryParamIfPresent("type", Optional.ofNullable(type))
                        .queryParamIfPresent("joinDateStart", Optional.ofNullable(joinDateStart))
                        .queryParamIfPresent("joinDateEnd", Optional.ofNullable(joinDateEnd))
                        .build())
                .header("requesterId", requesterId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Chat service error")))
                .bodyToMono(new ParameterizedTypeReference<List<ChatDto>>() {})
                .block();
    }

    public ChatDto getChatById(String requesterId, String chatId) {
        return webClient.get()
                .uri("/{chatId}", chatId)
                .header("requesterId", requesterId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Chat service error")))
                .bodyToMono(ChatDto.class)
                .block();
    }

    public ResponseEntity<Resource> getChatAvatarFull(String requesterId, String chatId) {
        return webClient.get()
                .uri("/{chatId}/avatar", chatId)
                .header("requesterId", requesterId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Chat service error")))
                .toEntity(Resource.class)
                .block();
    }

    public ChatDto createChat(String requesterId, ChatUserRequest request) {
        return webClient.post()
                .header("requesterId", requesterId)
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Chat service error")))
                .bodyToMono(ChatDto.class)
                .block();
    }

    public ChatDto updateChatName(String requesterId, String chatId, ChatUpdateNameRequest name) {
        return webClient.patch()
                .uri("/{chatId}/name", chatId)
                .header("requesterId", requesterId)
                .bodyValue(name)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Chat service error")))
                .bodyToMono(ChatDto.class)
                .block();
    }

    public String uploadChatAvatar(String requesterId, String chatId, MultipartFile file) {

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", file.getResource());

        return webClient.post()
                .uri("/{chatId}/avatar/upload", chatId)
                .header("requesterId", requesterId)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Chat service error")))
                .bodyToMono(String.class)
                .block();
    }

    public String deactivateChat(String requesterId, String chatId) {
        return webClient.delete()
                .uri("/{chatId}/deactivate", chatId)
                .header("requesterId", requesterId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Chat service error")))
                .bodyToMono(String.class)
                .block();
    }

}
