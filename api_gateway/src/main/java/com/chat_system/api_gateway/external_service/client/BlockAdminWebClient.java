package com.chat_system.api_gateway.external_service.client;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.chat_system.api_gateway.external_service.dto.response.social_service.BlockDto;
import com.chat_system.api_gateway.infrastructure.properties.WebClientProperties;
import com.chat_system.api_gateway.presentation.dto.request.block.BlockCreateRequest;

import reactor.core.publisher.Mono;

@Service
public class BlockAdminWebClient {
    private final WebClient webClient;

    public BlockAdminWebClient(WebClient.Builder builder, WebClientProperties properties) {
        this.webClient = builder.baseUrl(properties.social).build();
    }

    public List<BlockDto> getBlocks(
        int page,
        int size,
        boolean ascSort,
        Instant createdDateStart,
        Instant createdDateEnd
    ) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .queryParam("ascSort", ascSort)
                        .queryParamIfPresent("createdDateStart", Optional.ofNullable(createdDateStart))
                        .queryParamIfPresent("createdDateEnd", Optional.ofNullable(createdDateEnd))
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Block service error")))
                .bodyToMono(new ParameterizedTypeReference<List<BlockDto>>() {})
                .block();
    }

    public BlockDto getBlockById(String id) {
        return webClient.get()
                .uri("/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Block service error")))
                .bodyToMono(BlockDto.class)
                .block();
    }

    public List<BlockDto> getUserBlocked(
        String userId,
        int page,
        int size,
        boolean ascSort,
        Instant createdDateStart,
        Instant createdDateEnd
    ) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/user/{userId}/blocked")
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .queryParam("ascSort", ascSort)
                        .queryParamIfPresent("createdDateStart", Optional.ofNullable(createdDateStart))
                        .queryParamIfPresent("createdDateEnd", Optional.ofNullable(createdDateEnd))
                        .build(userId))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Block service error")))
                .bodyToMono(new ParameterizedTypeReference<List<BlockDto>>() {})
                .block();
    }

    public List<BlockDto> getUserBlocker(
        String userId,
        int page,
        int size,
        boolean ascSort,
        Instant createdDateStart,
        Instant createdDateEnd
    ) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/user/{userId}/blocker")
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .queryParam("ascSort", ascSort)
                        .queryParamIfPresent("createdDateStart", Optional.ofNullable(createdDateStart))
                        .queryParamIfPresent("createdDateEnd", Optional.ofNullable(createdDateEnd))
                        .build(userId))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Block service error")))
                .bodyToMono(new ParameterizedTypeReference<List<BlockDto>>() {})
                .block();
    }

    public BlockDto getUserBlockUser(String blockerId, String blockedId) {
        return webClient.get()
                .uri("/blocker/{blockerId}/blocked/{blockedId}", blockerId, blockedId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Block service error")))
                .bodyToMono(BlockDto.class)
                .block();
    }

    public BlockDto createBlock(BlockCreateRequest request) {
        return webClient.post()
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Block service error")))
                .bodyToMono(BlockDto.class)
                .block();
    }

    public void deleteBlock(String id) {
        webClient.delete()
                .uri("/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        r -> r.bodyToMono(String.class)
                            .map(RuntimeException::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new RuntimeException("Block service error")))
                .bodyToMono(Void.class)
                .block();
    }

}  
