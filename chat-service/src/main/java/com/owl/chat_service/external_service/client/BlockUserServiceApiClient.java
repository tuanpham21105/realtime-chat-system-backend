package com.owl.chat_service.external_service.client;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import com.owl.chat_service.external_service.dto.BlockDto;
import com.owl.chat_service.infrastructure.properties.ExternalServicesProperties;

import reactor.core.publisher.Mono;

@Component
public class BlockUserServiceApiClient {
    @Value("${intenal-api-key}")
    private String internalApiKey;

    private final WebClient webClient;

    public BlockUserServiceApiClient(WebClient.Builder builder, ExternalServicesProperties properties) {
        webClient = builder.baseUrl(properties.getSocial()).build();
    }

    public BlockDto getUserBlockUser(String blockerId, String blockedId) {
        return webClient.get().uri("/admin/block/blocker/" + blockerId + "/blocked/" + blockedId)
            .header("X-Internal-Api-Key", internalApiKey)
            .exchangeToMono(response -> {
                if (response == null)
                    return Mono.empty();

                if (response.statusCode().is2xxSuccessful()) {
                    // If body is empty this will become an empty Mono -> block() returns null
                    return response.bodyToMono(BlockDto.class);
                }
                if (response.statusCode().value() == 404) {
                    // convert 404 into empty result
                    return Mono.empty();
                }
                // propagate other errors as exceptions
                if (response.statusCode().is4xxClientError())
                    throw new RuntimeException("Block not found");

                if (response.statusCode().is5xxServerError())
                    throw new RuntimeException("Server not found");

                return Mono.empty();
            })
            .timeout(Duration.ofSeconds(3))
            .block();
    }
}
