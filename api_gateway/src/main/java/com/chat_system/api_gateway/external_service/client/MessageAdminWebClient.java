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
import org.springframework.web.reactive.function.client.WebClient;

import com.chat_system.api_gateway.external_service.dto.response.chat_service.MessageDto;
import com.chat_system.api_gateway.infrastructure.properties.WebClientProperties;
import com.chat_system.api_gateway.presentation.dto.request.message.MessageUpdateContentRequest;
import com.chat_system.api_gateway.presentation.dto.request.message.TextMessageAdminRequest;

import reactor.core.publisher.Mono;

@Service
public class MessageAdminWebClient {
    private final WebClient webClient;

    public MessageAdminWebClient(WebClient.Builder builder, WebClientProperties properties) {
        this.webClient = builder.baseUrl(properties.chat).build();
    }

    public List<MessageDto> getMessages(
        String keywords,
        int page,
        int size,
        boolean ascSort,
        Boolean status,
        String state,
        String type,
        Instant sentDateStart,
        Instant sentDateEnd,
        Instant removedDateStart,
        Instant removedDateEnd,
        Instant createdDateStart,
        Instant createdDateEnd
    ) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("keywords", keywords)
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .queryParam("ascSort", ascSort)
                        .queryParamIfPresent("status", Optional.ofNullable(status))
                        .queryParamIfPresent("state", Optional.ofNullable(state))
                        .queryParamIfPresent("type", Optional.ofNullable(type))
                        .queryParamIfPresent("sentDateStart", Optional.ofNullable(sentDateStart))
                        .queryParamIfPresent("sentDateEnd", Optional.ofNullable(sentDateEnd))
                        .queryParamIfPresent("removedDateStart", Optional.ofNullable(removedDateStart))
                        .queryParamIfPresent("removedDateEnd", Optional.ofNullable(removedDateEnd))
                        .queryParamIfPresent("createdDateStart", Optional.ofNullable(createdDateStart))
                        .queryParamIfPresent("createdDateEnd", Optional.ofNullable(createdDateEnd))
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Chat service error")))
                .bodyToMono(new ParameterizedTypeReference<List<MessageDto>>() {})
                .block();
    }

    public List<MessageDto> getMessagesByChatId(
        String chatId,
        String keywords,
        int page,
        int size,
        boolean ascSort,
        Boolean status,
        String state,
        String type,
        Instant sentDateStart,
        Instant sentDateEnd,
        Instant removedDateStart,
        Instant removedDateEnd,
        Instant createdDateStart,
        Instant createdDateEnd
    ) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/chat/{chatId}")
                        .queryParam("keywords", keywords)
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .queryParam("ascSort", ascSort)
                        .queryParamIfPresent("status", Optional.ofNullable(status))
                        .queryParamIfPresent("state", Optional.ofNullable(state))
                        .queryParamIfPresent("type", Optional.ofNullable(type))
                        .queryParamIfPresent("sentDateStart", Optional.ofNullable(sentDateStart))
                        .queryParamIfPresent("sentDateEnd", Optional.ofNullable(sentDateEnd))
                        .queryParamIfPresent("removedDateStart", Optional.ofNullable(removedDateStart))
                        .queryParamIfPresent("removedDateEnd", Optional.ofNullable(removedDateEnd))
                        .queryParamIfPresent("createdDateStart", Optional.ofNullable(createdDateStart))
                        .queryParamIfPresent("createdDateEnd", Optional.ofNullable(createdDateEnd))
                        .build(chatId))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Chat service error")))
                .bodyToMono(new ParameterizedTypeReference<List<MessageDto>>() {})
                .block();
    }

    public List<MessageDto> getMessagesBySenderId(
        String senderId,
        String keywords,
        int page,
        int size,
        boolean ascSort,
        Boolean status,
        String state,
        String type,
        Instant sentDateStart,
        Instant sentDateEnd,
        Instant removedDateStart,
        Instant removedDateEnd,
        Instant createdDateStart,
        Instant createdDateEnd
    ) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/sender/{senderId}")
                        .queryParam("keywords", keywords)
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .queryParam("ascSort", ascSort)
                        .queryParamIfPresent("status", Optional.ofNullable(status))
                        .queryParamIfPresent("state", Optional.ofNullable(state))
                        .queryParamIfPresent("type", Optional.ofNullable(type))
                        .queryParamIfPresent("sentDateStart", Optional.ofNullable(sentDateStart))
                        .queryParamIfPresent("sentDateEnd", Optional.ofNullable(sentDateEnd))
                        .queryParamIfPresent("removedDateStart", Optional.ofNullable(removedDateStart))
                        .queryParamIfPresent("removedDateEnd", Optional.ofNullable(removedDateEnd))
                        .queryParamIfPresent("createdDateStart", Optional.ofNullable(createdDateStart))
                        .queryParamIfPresent("createdDateEnd", Optional.ofNullable(createdDateEnd))
                        .build(senderId))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Chat service error")))
                .bodyToMono(new ParameterizedTypeReference<List<MessageDto>>() {})
                .block();
    }

    public MessageDto getMessageById(String messageId) {
        return webClient.get()
                .uri("/{messageId}", messageId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Chat service error")))
                .bodyToMono(MessageDto.class)
                .block();
    }

    public ResponseEntity<Resource> getMessageFile(String messageId) {
        return webClient.get()
                .uri("/{messageId}/resource", messageId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Chat service error")))
                .toEntity(Resource.class)
                .block();
    }

    public MessageDto createTextMessage(TextMessageAdminRequest request) {
        return webClient.post()
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Chat service error")))
                .bodyToMono(MessageDto.class)
                .block();
    }

    public String uploadFileMessage(
        String senderId,
        String chatId,
        String type,
        MultipartFile file
    ) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", file.getResource());

        return webClient.post()
                .uri("/resource/upload")
                .header("senderId", senderId)
                .header("chatId", chatId)
                .header("type", type)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .bodyValue(builder.build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Chat service error")))
                .bodyToMono(String.class)
                .block();
    }

    public MessageDto editTextMessage(
        String messageId,
        MessageUpdateContentRequest request
    ) {
        return webClient.put()
                .uri("/{messageId}/edit", messageId)
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Chat service error")))
                .bodyToMono(MessageDto.class)
                .block();
    }

    public MessageDto activateMessage(String messageId) {
        return webClient.patch()
                .uri("/{messageId}/activate", messageId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Chat service error")))
                .bodyToMono(MessageDto.class)
                .block();
    }

    public String softDeleteMessage(String messageId) {
        return webClient.delete()
                .uri("/{messageId}/remove", messageId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Chat service error")))
                .bodyToMono(String.class)
                .block();
    }

    public String hardDeleteMessage(String messageId) {
        return webClient.delete()
                .uri("/{messageId}", messageId)
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
