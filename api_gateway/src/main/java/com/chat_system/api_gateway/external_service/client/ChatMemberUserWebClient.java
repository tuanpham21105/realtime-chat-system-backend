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
import com.chat_system.api_gateway.presentation.dto.request.chat_member.ChatMemberCreateUserRequest;
import com.chat_system.api_gateway.presentation.dto.request.chat_member.ChatMemberUpdateNicknameRequest;
import com.chat_system.api_gateway.presentation.dto.request.chat_member.ChatMemberUpdateRoleRequest;

import reactor.core.publisher.Mono;

@Service
public class ChatMemberUserWebClient {
    private final WebClient webClient;

    public ChatMemberUserWebClient(WebClient.Builder builder, WebClientProperties properties) {
        this.webClient = builder.baseUrl(properties.chat).build();
    }

    public List<ChatMemberDto> getChatMembersByMemberId(
        String requesterId,
        String keywords,
        int page,
        int size,
        boolean ascSort,
        String role,
        Instant joinDateStart,
        Instant joinDateEnd
    ) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("keywords", keywords)
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .queryParam("ascSort", ascSort)
                        .queryParamIfPresent("role", Optional.ofNullable(role))
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
                .bodyToMono(new ParameterizedTypeReference<List<ChatMemberDto>>() {})
                .block();
    }

    public List<ChatMemberDto>getChatMembersByChatId(
        String requesterId,
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
                .uri(uriBuilder -> uriBuilder
                        .path("/chat/{chatId}")
                        .queryParam("keywords", keywords)
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .queryParam("ascSort", ascSort)
                        .queryParamIfPresent("role", Optional.ofNullable(role))
                        .queryParamIfPresent("joinDateStart", Optional.ofNullable(joinDateStart))
                        .queryParamIfPresent("joinDateEnd", Optional.ofNullable(joinDateEnd))
                        .build(chatId))
                .header("requesterId", requesterId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Chat service error")))
                .bodyToMono(new ParameterizedTypeReference<List<ChatMemberDto>>() {})
                .block();
    }

    public ChatMemberDto getChatMemberByChatIdAndMemberId(
        String requesterId,
        String memberId,
        String chatId
    ) {
        return webClient.get()
                .uri("/{memberId}/chat/{chatId}", memberId, chatId)
                .header("requesterId", requesterId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Chat service error")))
                .bodyToMono(ChatMemberDto.class)
                .block();
    }

    public ChatMemberDto createChatMember(
        String requesterId,
        ChatMemberCreateUserRequest request
    ) {
        return webClient.post()
                .uri("")
                .header("requesterId", requesterId)
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

    public ChatMemberDto updateRole(
        String requesterId,
        String memberId,
        String chatId,
        ChatMemberUpdateRoleRequest request
    ) {
        return webClient.patch()
                .uri("/{memberId}/chat/{chatId}/role", memberId, chatId)
                .header("requesterId", requesterId)
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

    public ChatMemberDto updateNickname(
        String requesterId,
        String memberId,
        String chatId,
        ChatMemberUpdateNicknameRequest request
    ) {
        return webClient.patch()
                .uri("/{memberId}/chat/{chatId}/nickname", memberId, chatId)
                .header("requesterId", requesterId)
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

    public String deleteChatMember(
        String requesterId,
        String memberId,
        String chatId
    ) {
        return webClient.delete()
                .uri("/{memberId}/chat/{chatId}", memberId, chatId)
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
