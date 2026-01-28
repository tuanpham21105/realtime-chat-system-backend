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
import com.chat_system.api_gateway.presentation.dto.request.chat.ChatAdminRequest;

import reactor.core.publisher.Mono;

@Service
public class ChatAdminWebClient {
    private final WebClient webClient;

    public ChatAdminWebClient(WebClient.Builder builder, WebClientProperties properties) {
        this.webClient = builder.baseUrl(properties.chat).build();
    }

    public List<ChatDto> getChats(
        String keywords,
        int page,
        int size,
        boolean ascSort,
        Boolean status,
        String type,
        String initiatorId,
        Instant createdDateStart,
        Instant createdDateEnd
    ) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParamIfPresent("keywords", Optional.ofNullable(keywords))
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .queryParam("ascSort", ascSort)
                        .queryParamIfPresent("status", Optional.ofNullable(status))
                        .queryParamIfPresent("type", Optional.ofNullable(type))
                        .queryParamIfPresent("initiatorId", Optional.ofNullable(initiatorId))
                        .queryParamIfPresent("createdDateStart", Optional.ofNullable(createdDateStart))
                        .queryParamIfPresent("createdDateEnd", Optional.ofNullable(createdDateEnd))
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Chat service error")))
                .bodyToMono(new ParameterizedTypeReference<List<ChatDto>>() {})
                .block();
    }

    public ChatDto getChatById(String chatId) {
        return webClient.get()
                .uri("/{chatId}", chatId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Chat service error")))
                .bodyToMono(ChatDto.class)
                .block();
    }

    public ResponseEntity<Resource> getChatAvatarWithType(String chatId) {
        return webClient.get()
                .uri("/{chatId}/avatar", chatId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Chat service error")))
                .toEntity(Resource.class)
                .block();
    }

    public ChatDto createChat(ChatAdminRequest request) {
        return webClient.post()
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

    public ChatDto updateChat(String chatId, ChatAdminRequest request) {
        return webClient.put()
                .uri("/{chatId}", chatId)
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

    public ChatDto updateChatStatus(String chatId, boolean status) {
        return webClient.patch()
                .uri("/{chatId}/status", chatId)
                .bodyValue(status)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Chat service error")))
                .bodyToMono(ChatDto.class)
                .block();
    }

    public String uploadChatAvatar(String chatId, MultipartFile file) {

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", file.getResource());

        return webClient.post()
                .uri("/{chatId}/avatar/upload", chatId)
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

    public String deleteChat(String chatId) {
        return webClient.delete()
                .uri("/{chatId}", chatId)
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
