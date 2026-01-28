package com.chat_system.api_gateway.external_service.client;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.chat_system.api_gateway.external_service.dto.response.chat_service.ChatMemberDto;
import com.chat_system.api_gateway.infrastructure.properties.WebClientProperties;
import com.chat_system.api_gateway.presentation.dto.request.chat_member.ChatMemberAdminRequest;
import reactor.core.publisher.Mono;

@Service
public class ChatMemberAdminWebClient {
    private final WebClient webClient;

    public ChatMemberAdminWebClient(WebClient.Builder builder, WebClientProperties properties) {
        this.webClient = builder.baseUrl(properties.chat).build();
    }

    public List<ChatMemberDto> getChatMembers(
        String keywords,
        int page,
        int size,
        boolean ascSort,
        String role,
        Instant joinDateStart,
        Instant joinDateEnd
    ) {
        return webClient.get()
                .uri(uri -> uri
                        .queryParamIfPresent("keywords", Optional.ofNullable(keywords))
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .queryParam("ascSort", ascSort)
                        .queryParamIfPresent("role", Optional.ofNullable(role))
                        .queryParamIfPresent("joinDateStart", Optional.ofNullable(joinDateStart))
                        .queryParamIfPresent("joinDateEnd", Optional.ofNullable(joinDateEnd))
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Chat service error")))
                .bodyToMono(new ParameterizedTypeReference<List<ChatMemberDto>>() {})
                .block();
    }

    public List<ChatMemberDto> getChatMembersByChatId(
        String chatId,
        String keywords,
        int page,
        int size,
        boolean ascSort,
        String role,
        Instant joinDateStart,
        Instant joinDateEnd
    ) {
        return webClient.get()
                .uri(uri -> uri
                        .path("/chat/{chatId}")
                        .queryParamIfPresent("keywords", Optional.ofNullable(keywords))
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .queryParam("ascSort", ascSort)
                        .queryParamIfPresent("role", Optional.ofNullable(role))
                        .queryParamIfPresent("joinDateStart", Optional.ofNullable(joinDateStart))
                        .queryParamIfPresent("joinDateEnd", Optional.ofNullable(joinDateEnd))
                        .build(chatId))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Chat service error")))
                .bodyToMono(new ParameterizedTypeReference<List<ChatMemberDto>>() {})
                .block();
    }

    public List<ChatMemberDto> getChatMembersByMemberId(
        String memberId,
        String keywords,
        int page,
        int size,
        boolean ascSort,
        String role,
        Instant joinDateStart,
        Instant joinDateEnd
    ) {
        return webClient.get()
                .uri(uri -> uri
                        .path("/{memberId}")
                        .queryParamIfPresent("keywords", Optional.ofNullable(keywords))
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .queryParam("ascSort", ascSort)
                        .queryParamIfPresent("role", Optional.ofNullable(role))
                        .queryParamIfPresent("joinDateStart", Optional.ofNullable(joinDateStart))
                        .queryParamIfPresent("joinDateEnd", Optional.ofNullable(joinDateEnd))
                        .build(memberId))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Chat service error")))
                .bodyToMono(new ParameterizedTypeReference<List<ChatMemberDto>>() {})
                .block();
    }

    public ChatMemberDto getChatMemberByChatAndMember(
        String chatId,
        String memberId
    ) {
        return webClient.get()
                .uri("/{memberId}/chat/{chatId}", memberId, chatId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Chat service error")))
                .bodyToMono(ChatMemberDto.class)
                .block();
    }

    public ChatMemberDto createChatMember(ChatMemberAdminRequest request) {
        return webClient.post()
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Chat service error")))
                .bodyToMono(ChatMemberDto.class)
                .block();
    }

    public ChatMemberDto updateChatMember(
        String chatId,
        String memberId,
        ChatMemberAdminRequest request
    ) {
        return webClient.put()
                .uri("/{memberId}/chat/{chatId}", memberId, chatId)
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Chat service error")))
                .bodyToMono(ChatMemberDto.class)
                .block();
    }

    public ChatMemberDto updateChatMemberRole(
        String chatId,
        String memberId,
        String role
    ) {
        return webClient.patch()
                .uri("/{memberId}/chat/{chatId}/role", memberId, chatId)
                .bodyValue(role)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Chat service error")))
                .bodyToMono(ChatMemberDto.class)
                .block();
    }

    public ChatMemberDto updateChatMemberNickname(
            String chatId,
            String memberId,
            String nickname
    ) {
        return webClient.patch()
                .uri("/{memberId}/chat/{chatId}/nickname", memberId, chatId)
                .bodyValue(nickname)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Chat service error")))
                .bodyToMono(ChatMemberDto.class)
                .block();
    }

    public String deleteChatMember(String chatId, String memberId) {
        return webClient.delete()
                .uri("/{memberId}/chat/{chatId}", memberId, chatId)
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
