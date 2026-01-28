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
import com.chat_system.api_gateway.presentation.dto.request.message.TextMessageUserRequest;

import reactor.core.publisher.Mono;

@Service
public class MessageUserWebClient {
    private final WebClient webClient;

    public MessageUserWebClient(WebClient.Builder builder, WebClientProperties properties) {
        this.webClient = builder.baseUrl(properties.chat).build();
    }

    public List<MessageDto> getMessagesByChatId(
        String requesterId,
        String chatId,
        String keywords,
        int page,
        int size,
        boolean ascSort,
        String type,
        String senderId,
        Instant sentDateStart,
        Instant sentDateEnd
    ) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/chat/{chatId}")
                        .queryParam("keywords", keywords)
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .queryParam("ascSort", ascSort)
                        .queryParam("type", type)
                        .queryParam("senderId", senderId)
                        .queryParamIfPresent("sentDateStart", Optional.ofNullable(sentDateStart))
                        .queryParamIfPresent("sentDateEnd", Optional.ofNullable(sentDateEnd))
                        .build(chatId))
                .header("requesterId", requesterId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Chat service error")))
                .bodyToMono(new ParameterizedTypeReference<List<MessageDto>>() {})
                .block();
    }

    public MessageDto getMessageById(
        String requesterId,
        String messageId
    ) {
        return webClient.get()
                .uri("/{messageId}", messageId)
                .header("requesterId", requesterId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Chat service error")))
                .bodyToMono(MessageDto.class)
                .block();
    }

    public ResponseEntity<Resource> getMessageFile(String requesterId,String messageId) {
        return webClient.get()
                .uri("/{messageId}/resource", messageId)
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

    public MessageDto createTextMessage(
        String requesterId,
        TextMessageUserRequest request
    ) {
        return webClient.post()
                .header("requesterId", requesterId)
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
        String requesterId,
        String chatId,
        String type,
        MultipartFile file
    ) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", file.getResource());

        return webClient.post()
                .uri("/resource/upload")
                .header("requesterId", requesterId)
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

    public MessageDto editMessage(
        String requesterId,
        String messageId,
        MessageUpdateContentRequest request
    ) {
        return webClient.put()
                .uri("/{messageId}/edit", messageId)
                .header("requesterId", requesterId)
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

    public MessageDto softDeleteMessage(
        String requesterId,
        String messageId
    ) {
        return webClient.delete()
                .uri("/{messageId}", messageId)
                .header("requesterId", requesterId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Chat service error")))
                .bodyToMono(MessageDto.class)
                .block();
    }

}
